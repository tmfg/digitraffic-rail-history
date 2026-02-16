import { expect, test } from "@playwright/test";
import {
  checkHeaderRow,
  checkTableExists,
  checkTableHasData,
  getPage,
  openTrainHistoryPage,
  submitTrainInfoForm,
} from "./common-steps";
import { getLatestCompositionInfo, getLatestTrainInfo } from "./db-helper";

// Check if running local test
const isLocalTest = process.env.IS_LOCAL_TEST_RUN === "true";

// Hardcoded fallback values for CI/remote
const fallbackTrain = {
  train_number: 9154,
  departure_date: "2015-12-19",
};

test("Train timetable page finds results", async ({ browser }) => {
  const page = await getPage(browser);
  await openTrainHistoryPage(page);
  await test.step("Search for train schedules", async () => {
    const aikataulujaButton = page.getByRole("link", { name: "Aikatauluja" });
    await aikataulujaButton.click();

    const latestTrain = isLocalTest
      ? await getLatestTrainInfo()
      : { train_number: 46, departure_date: new Date().toISOString() };
    console.log(`Latest train (isLocalTest: ${isLocalTest}): ${JSON.stringify(latestTrain)}`);

    await submitTrainInfoForm(
      page,
      latestTrain.train_number,
      new Date(latestTrain.departure_date),
      "Etsi aikataulu",
    );
    //await submitTrainInfoForm(page, 9154, new Date(2015, 11, 19, 12), "Etsi aikataulu");
  });
  await test.step("Check search results", async () => {
    const versioDropdown = page.getByRole("combobox", { name: "Versio" });
    await expect(versioDropdown).toBeVisible();

    const infoTextContainer = page.locator(".version-details");
    const operaattoriInfoText = infoTextContainer
      .filter({ hasText: "Operaattori" })
      .first();
    const tyyppiInfoText = infoTextContainer
      .filter({ hasText: "Tyyppi" })
      .first();
    const hyvaksyttyInfoText = infoTextContainer
      .filter({ hasText: "Hyväksytty" })
      .first();
    const kulussaInfoText = infoTextContainer
      .filter({ hasText: "Kulussa" })
      .first();
    const peruttuInfoText = infoTextContainer
      .filter({ hasText: "Peruttu" })
      .first();
    for (const infoText of [
      operaattoriInfoText,
      tyyppiInfoText,
      hyvaksyttyInfoText,
      kulussaInfoText,
      peruttuInfoText,
    ]) {
      await expect(infoText).toBeVisible();
    }

    const resultTable = page.getByRole("table");
    const headerTexts = [
      "Liikennepaikka",
      "Raide",
      "Tyyppi",
      "Aikatauluaika",
      "Toteutunut aika",
      "Ennuste",
      "Ennusteen lähde",
      "Ennuste epävarma",
      "Raide epävarma",
      "Peruttu",
      "Pysähdyssektori",
      "Lähtövalmius",
      "Syykoodit",
    ];
    await checkTableExists(resultTable);
    await checkHeaderRow(resultTable, headerTexts);
    await checkTableHasData(resultTable);
  });
});
