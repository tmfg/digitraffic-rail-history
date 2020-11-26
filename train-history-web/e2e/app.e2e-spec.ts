import { TrainHistoryWebPage } from './app.po';

describe('train-history-web App', () => {
  let page: TrainHistoryWebPage;

  beforeEach(() => {
    page = new TrainHistoryWebPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!!');
  });
});
