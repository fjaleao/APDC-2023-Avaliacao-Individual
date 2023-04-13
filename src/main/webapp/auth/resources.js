function getURL(resourcePath) {
    return `${window.location.protocol}//${window.location.host}/rest/${resourcePath}`
}

// #region Register resources

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

  function submitFormAJAX(event) {
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
    const inputValidation = document.getElementById('input-validation');

    // Validate password
    if (password.length < 8) {
      passwordValidation.style.display = 'block';
      return;
    } else
      passwordValidation.style.display = 'none';
    
    // Validate mandatory inputs
    if (username == null || email == null || password == null || password_verify == null) {
      inputValidation.style.display = 'block';
      return;
    } else
      inputValidation.style.display = 'none';

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

    // Get target URL
    const url = getURL("register/v2")
    
    // Send JSON data to server using AJAX
    const xhr = new XMLHttpRequest();
    xhr.open('POST', url);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onreadystatechange = function() {
      if (xhr.status === 200) {
        console.log('Form submitted successfully:', xhr.responseText);
      } else {
        console.error('Error submitting form:', xhr.statusText);
      }
    };
    xhr.onerror = function() {
      console.error('Error submitting form:', xhr.statusText);
    };
    xhr.send(JSON.stringify(json));
  }

  function submitFormFetch(event) {
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
    const inputValidation = document.getElementById('input-validation');

    // Validate password
    if (password.length < 8) {
      passwordValidation.style.display = 'block';
      return;
    } else
      passwordValidation.style.display = 'none';
    
    // Validate mandatory inputs
    if (username == null || email == null || password == null || password_verify == null) {
      inputValidation.style.display = 'block';
      return;
    } else
      inputValidation.style.display = 'none';

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
    
    // Send data to server
    // var url = getURL("register/v2");
    var url = '/rest/register/v2';
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
  }

// #endregion Register Resources