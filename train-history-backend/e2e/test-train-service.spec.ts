import { test, expect } from '@playwright/test';
import {
  getPage,
  openTrainHistoryPage,
  submitTrainInfoForm,
  checkHeaderRow,
  checkTableExists,
  checkTableHasData
} from './common-steps';

test('Train page finds results', async ({ browser }) => {
  const page = await getPage(browser)
  await openTrainHistoryPage(page)
  await test.step('Search for train schedules', async () => {
    const aikataulujaButton = page.getByRole('link', {name: 'Aikatauluja'});
    await aikataulujaButton.click();

    await submitTrainInfoForm(page, 1, new Date(), "Etsi aikataulu");
    //await submitTrainInfoForm(page, 9154, new Date(2015, 11, 19, 12), "Etsi aikataulu");
  })
  await test.step('Check search results', async () => {
    const versioDropdown = page.getByRole('combobox', {name: 'Versio'});
    await expect(versioDropdown).toBeVisible();

    const infoTextContainer = page.locator('.version-details')
    const operaattoriInfoText = infoTextContainer.filter({hasText: 'Operaattori'}).first();
    const tyyppiInfoText = infoTextContainer.filter({hasText: 'Tyyppi'}).first();
    const hyvaksyttyInfoText = infoTextContainer.filter({hasText: 'Hyväksytty'}).first();
    const kulussaInfoText = infoTextContainer.filter({hasText: 'Kulussa'}).first();
    const peruttuInfoText = infoTextContainer.filter({hasText: 'Peruttu'}).first();
    for (const infoText of [operaattoriInfoText, tyyppiInfoText, hyvaksyttyInfoText, kulussaInfoText, peruttuInfoText, lahilinjatunnusInfoText]) {
      await expect(infoText).toBeVisible();
    }

    const resultTable = page.getByRole('table');
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
      "Syykoodit"
    ];
    await checkTableExists(resultTable)
    await checkHeaderRow(resultTable, headerTexts)
    await checkTableHasData(resultTable)
  })
});
