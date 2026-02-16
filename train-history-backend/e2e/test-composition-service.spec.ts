import { expect, test } from "@playwright/test";
import {
  checkHeaderRow,
  checkTableExists,
  checkTableHasData,
  getPage,
  openTrainHistoryPage,
  submitTrainInfoForm,
} from "./common-steps";
import { getLatestCompositionInfo } from "./db-helper";

// Check if running local test
const isLocalTest = process.env.IS_LOCAL_TEST_RUN === "true";

test("Train compositions page finds results", async ({ browser }) => {
  const page = await getPage(browser);
  await openTrainHistoryPage(page);
  await test.step("Search for train compositions", async () => {
    const kokoonpanojaButton = page.getByRole("link", { name: "Kokoonpanoja" });
    await kokoonpanojaButton.click();

    const latestComposition = isLocalTest
      ? await getLatestCompositionInfo()
      : { train_number: 46, departure_date: new Date().toISOString() };
    console.log(`Latest composition (isLocalTest: ${isLocalTest}): ${JSON.stringify(latestComposition)}`);

    await submitTrainInfoForm(
      page,
      latestComposition.train_number,
      new Date(latestComposition.departure_date),
      "Etsi kokoonpano",
    );
    //await submitTrainInfoForm(page, 7, new Date(2015, 11, 29, 12), "Etsi kokoonpano");
  });
  await test.step("Check search results", async () => {
    const versioDropdown = page.getByRole("combobox", { name: "Versio" });
    await expect(versioDropdown).toBeVisible();

    const infoText = page
      .locator(".version-details")
      .filter({ hasText: "Junatyyppi" })
      .first();
    await expect(infoText).toBeVisible();

    const resultTable = page.getByRole("table");
    const headerTexts = [
      "Pituus",
      "Maksiminopeus",
      "Lähtö",
      "Saapuminen",
      "Veturit",
      "Vaunut",
    ];
    await checkTableExists(resultTable);
    await checkHeaderRow(resultTable, headerTexts);
    await checkTableHasData(resultTable);
  });
});
