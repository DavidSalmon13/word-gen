chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.type === "SHOW_POPUP") {
    window.selectedTextFromContext = message.selectedText; // Save the highlighted text
    document.body.appendChild(popup); // Show the popup
  }
});

// Create popup wrapper
const popup = document.createElement('div');
popup.className = 'popup';
popup.style = `
  display: flex;
  position: fixed;
  top: 0; left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  justify-content: center;
  align-items: center;
  z-index: 999999;
`;

// Create popup content
const content = document.createElement('div');
content.className = 'popup-content';
content.style = `
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  text-align: center;
`;

// Inner HTML with contact fields
content.innerHTML = `
  <h2>Enter your description</h2>
  <textarea id="userDescription" rows="3" cols="40" placeholder="Describe yourself..."></textarea><br><br>
  <input type="email" id="userEmail" placeholder="Email (optional)" style="width: 90%; padding: 5px;"><br><br>
  <input type="tel" id="userPhone" placeholder="Phone (optional)" style="width: 90%; padding: 5px;"><br><br>
  <input type="text" id="userAddress" placeholder="Address (optional)" style="width: 90%; padding: 5px;"><br><br>
  <input type="name" id="userName" placeholder="Name (optional)" style="width: 90%; padding: 5px;"><br><br>

  <button id="submitBtn">Submit</button>
  <button id="closeBtn" style="background: red; color: white; padding: 5px 10px; border: none; cursor: pointer;">Close</button>
`;

// Append content to popup
popup.appendChild(content);

// Close button functionality
content.querySelector('#closeBtn').onclick = () => popup.remove();

// Submit button functionality
content.querySelector('#submitBtn').onclick = () => {
  const userDescription = document.getElementById('userDescription').value;
  const email = document.getElementById('userEmail').value;
  const phone = document.getElementById('userPhone').value;
  const address = document.getElementById('userAddress').value;
  const name = document.getElementById('userName').value;


  const message = window.selectedTextFromContext || "";

  popup.remove();
  localStorage.setItem('userDescription', userDescription);
  localStorage.setItem('userEmail', email);
  localStorage.setItem('userPhone', phone);
  localStorage.setItem('userAddress', address);
  localStorage.setItem('userName', name);



  fetch(`http://localhost:4567/write?message=${encodeURIComponent(message)}&userDescription=${encodeURIComponent(userDescription)}&email=${encodeURIComponent(email)}&phone=${encodeURIComponent(phone)}&address=${encodeURIComponent(address)}&name=${encodeURIComponent(name)}`)
    .then(res => res.text())
    .then(data => {
      alert('Success: ' + data);
    })
    .catch(err => {
      console.error('Fetch error:', err);
    });
};





//--------------------------------------------------------------------
/*
// Listen for message from background.js
chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.type === "SHOW_POPUP") {
    window.selectedTextFromContext = message.selectedText;  // Save the highlighted text
    document.body.appendChild(popup);  // Show the popup
  }
});

// Create popup wrapper
const popup = document.createElement('div');
popup.className = 'popup';
popup.style = `
  display: flex;
  position: fixed;
  top: 0; left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  justify-content: center;
  align-items: center;
  z-index: 999999;
`;

// Create popup content
const content = document.createElement('div');
content.className = 'popup-content';
content.style = `
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  text-align: center;
`;

// Inner HTML
content.innerHTML = `
  <h2>Enter your description</h2>
  <textarea id="userDescription" rows="5" cols="40" placeholder="Describe yourself or the job..."></textarea><br><br>
  <button id="submitBtn">Submit</button>
  <button id="closeBtn" style="background: red; color: white; padding: 5px 10px; border: none; cursor: pointer;">Close</button>
`;

// Append to popup
popup.appendChild(content);

// Add behavior
content.querySelector('#closeBtn').onclick = () => popup.remove();

content.querySelector('#submitBtn').onclick = () => {
  // Get the value the user entered
  const userDescription = document.getElementById('userDescription').value;

  // The selected text is automatically passed as the 'message'
  const message = window.selectedTextFromContext || "";
  //********* */
  /*
  popup.remove();
  localStorage.setItem('userDescription', userDescription);
//************************** */
  // Send both message and user description to the backend
  /*
  fetch(`http://localhost:4567/write?message=${encodeURIComponent(message)}&userDescription=${encodeURIComponent(userDescription)}`)
    .then(res => res.text())
    .then(data => {
      alert('Success: ' + data);
      popup.remove();
    })
    .catch(err => {
      console.error('Fetch error:', err);
    });
};
*/