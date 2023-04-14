function getResource(resourcePath) {
  return `/rest/${resourcePath}`
}

function togglePasswordVisibility(passwordId) {
  const passwordInput = document.getElementById(passwordId);
  const toggleButton = passwordInput.nextElementSibling;
  
  if (passwordInput.type === 'password') {
    passwordInput.type = 'text';
    toggleButton.textContent = 'Hide';
  } else {
    passwordInput.type = 'password';
    toggleButton.textContent = 'Show';
  }
}

function redirectToLogin() {
  window.location.pathname = 'auth/login.html';
}

function isEmailValid(email) {
  const emailPattern = /^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/;
  return emailPattern.test(email)
}

// #region Register resources

function submitRegisterFormAJAX(event) {
  event.preventDefault(); // Prevent default form submission
  
  // Get form data
  const form = document.querySelector('form');
  const formData = new FormData(form);

  // Get form values
  const username = formData.get('username');
  const email = formData.get('email');
  const password = formData.get('password');
  const passwordValidation = document.getElementById('password-validation');
  const password_verify = formData.get('password_verify');
  const passwordConfirmation = document.getElementById('password-confirmation');
  const emailValidation = document.getElementById('email-validation');

  // Validate password
  if (password.length < 8) {
    passwordValidation.style.display = 'block';
    return;
  } else
    passwordValidation.style.display = 'none';
  
  // Validate mandatory inputs
  if (!isEmailValid(email)) {
    emailValidation.style.display = 'block';
    return;
  } else
    emailValidation.style.display = 'none';

  // Verify password
  if (password !== password_verify) {
    passwordConfirmation.style.display = 'block';
    return;
  } else
    passwordConfirmation.style.display = 'none';

  console.log("Input valid!");
  
  // Create JSON object
  const json = {
    username : username,
    email : email,
    password : password
  };

  console.log(json)

  // Target URL
  // const url = getResource("register/");
  const url = '/rest/register/';
  
  // Send JSON data to server using AJAX
  const xhr = new XMLHttpRequest();
  xhr.open('POST', url);
  xhr.setRequestHeader('Content-Type', 'application/json');
  xhr.onreadystatechange = function() {
    if (this.readyState == 4 && xhr.status === 200) {
      console.log('Form submitted successfully:', xhr.responseText);
    } else {
      console.error('Error submitting form:', xhr.statusText);
    }
  };
  xhr.onerror = function() {
    console.error('Error submitting form:', xhr.statusText);
  };
  xhr.send(JSON.stringify(json));

  window.location.pathname = 'auth/login.html';

  alert("User created successfully! Please log in to continue.")
}

function submitRegisterFormFetch(event) {
  event.preventDefault(); // Prevent default form submission
  
  // Get form data
  const form = document.querySelector('form');
  const formData = new FormData(form);

  // Get form values
  const username = formData.get('username');
  const email = formData.get('email');
  const password = formData.get('password');
  const passwordValidation = document.getElementById('password-validation');
  const password_verify = formData.get('password_verify');
  const passwordConfirmation = document.getElementById('password-confirmation');
  const emailValidation = document.getElementById('email-validation');

  // Validate password
  if (password.length < 8) {
    passwordValidation.style.display = 'block';
    return;
  } else
    passwordValidation.style.display = 'none';
  
  // Verify email format
  if (!isEmailValid(email)) {
    emailValidation.style.display = 'block';
    return;
  } else
    emailValidation.style.display = 'none';

  // Verify password
  if (password !== password_verify) {
    passwordConfirmation.style.display = 'block';
    return;
  } else
    passwordConfirmation.style.display = 'none';

  console.log("Input valid!");
  
  // Create JSON object
  const json = {
    username : username,
    email : email,
    password : password
  };
  
  // Send data to server
  // const url = getResource("register/");
  const url = '/rest/register/';
  console.log();
  fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json;charset=UTF-8"
    },
    body: JSON.stringify(json)
  })
  .then(response => {
    if (response.ok) {
      console.log("Registration successful!");
    } else {
      console.log("Registration failed.");
    }
  })
  .catch(error => {
    console.log("An error occurred while registering.");
  });

  redirectToLogin()

  alert("User created successfully! Please log in to continue.")
}

// #endregion Register Resources

// #region Login resources

function submitLoginFormAJAX(event) {
  event.preventDefault(); // Prevent default form submission
  
  // Get form data
  const form = document.querySelector('form');
  const formData = new FormData(form);

  // Get form values
  const username = formData.get('username');
  const password = formData.get('password');
  
  // Create JSON object
  const json = {
    username : username,
    password : password
  };

  console.log(json)

  // Target URL
  // const url = getResource("register/");
  const url = '/rest/login/';
  
  // Send JSON data to server using AJAX
  const xhr = new XMLHttpRequest();
  xhr.open('POST', url);
  xhr.setRequestHeader('Content-Type', 'application/json');
  xhr.onreadystatechange = function() {
    if (this.readyState == 4 && xhr.status === 200) {
      const token = JSON.parse(this.responseText);
      sessionStorage.setItem('token', JSON.stringify(token));
    } else {
      console.error('Error submitting form:', xhr.statusText);
    }
  };
  xhr.onerror = function() {
    console.error('Error submitting form:', xhr.statusText);
  };
  xhr.send(JSON.stringify(json));

}

function submitLoginFormFetch(event) {

}

// #endregion Login resources