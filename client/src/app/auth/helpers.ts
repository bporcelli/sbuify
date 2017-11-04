export function tokenGetter() {
  let rememberMe = localStorage.getItem('remember');

  // token is stored in session storage if remember is not ticked, local storage o.w.
  if (rememberMe == 'true') {
    return (() => localStorage.getItem('token'));
  } else {
    return (() => sessionStorage.getItem('token'));
  }
}
