function getURL() {
    return window.location.protocol + "//" + window.location.host
}

// #region Register resources

function togglePasswordVisibility() {
    const passwordInput = document.getElementById('password');
    const passwordValidation = document.getElementById('password-validation');
    const toggleButton = passwordInput.nextElementSibling;
    
    if (passwordInput.type === 'password') {
      passwordInput.type = 'text';
      passwordValidation.style.display = 'block';
      toggleButton.textContent = 'Hide';
    } else {
      passwordInput.type = 'password';
      passwordValidation.style.display = 'none';
      toggleButton.textContent = 'Show';
    }
  }
  function submitForm(event) {
    event.preventDefault(); // Prevent default form submission
    
    // Get form data
    const form = document.querySelector('form');
    const formData = new FormData(form);
    
    // Validate password
    const password = formData.get('password');
    const passwordValidation = document.getElementById('password-validation');
    if (password.length < 8) {
      passwordValidation.style.display = 'block';
      return;
    } else {
      passwordValidation.style.display = 'none';
    }
    
    // Convert form data to JSON object
    const json = {};
    formData.forEach((value, key) => json[key] = value);

    // Get target URL
    const url = getURL() + "/register/v2"
    
    // Send JSON data to server using AJAX
    const xhr = new XMLHttpRequest();
    xhr.open('POST', url, true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function() {
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

// #endregion Register Resources