import { SBUifyPage } from './app.po';

describe('sbuify App', () => {
  let page: SBUifyPage;

  beforeEach(() => {
    page = new SBUifyPage();
  });

  it('should display welcome message', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('Welcome to app!!');
  });
});
