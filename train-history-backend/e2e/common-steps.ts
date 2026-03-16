import {test, expect, type Browser, type Page, type Locator} from '@playwright/test';

export async function getPage(browser: Browser): Promise<Page> {
  return await browser.newPage({extraHTTPHeaders: {"Digitraffic-User": "internal-digitraffic-sitemonitor"}})
}

export async function openTrainHistoryPage(page: Page): Promise<void> {
  await test.step('Open train history page', async () => {
    await page.goto('/history');
    if (process.env.IS_LOCAL_TEST_RUN !== 'true') {
      // Decline cookies
      await page.getByRole('alert', {name: 'Decline all'}).click()
    }
  })
}

export async function submitTrainInfoForm(
  page: Page,
  trainNumber: number,
  date: Date,
  searchText: string,
): Promise<void> {
  const junanumeroInput = page.getByLabel('Junanumero');
  console.debug(`trainNumber: ${trainNumber.toString()}`)
  await junanumeroInput.fill(trainNumber.toString());

  const searchDate = (date).toISOString().split('T')[0]!
  const lahtopaivamaaraInput = page.getByLabel('Lähtöpäivämäärä');
  console.debug(`searchDate: ${searchDate}`)
  await lahtopaivamaaraInput.fill(searchDate);

  const etsiAikatauluButton = page.getByRole('button', {name: searchText});
  await etsiAikatauluButton.click();
}

export async function checkTableExists(table: Locator): Promise<void> {
  await expect(table).toBeVisible();
}

export async function checkHeaderRow(table: Locator, expectedHeaders: string[]): Promise<void> {
  const headers = table.getByRole('columnheader');
  const headerCount = await headers.count();
  expect(headerCount).toBe(expectedHeaders.length);
  for (const [i, headerText] of expectedHeaders.entries()) {
    const header = headers.nth(i);
    await expect(header).toHaveText(headerText);
  }
}

export async function checkTableHasData(table: Locator): Promise<void> {
  const rows = table.getByRole('row');
  const rowCount = await rows.count();
  expect(rowCount).toBeGreaterThan(1);
}