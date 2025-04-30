chrome.runtime.onMessage.addListener((message, sender, sendResponse) => {
  if (message.type === "SHOW_POPUP") {
    window.selectedTextFromContext = message.selectedText;  // Save the highlighted text
    showPopupWithSelectedText(message.selectedText);
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

content.innerHTML = `
  <h2>Enter your details</h2>
  <textarea id="jobDesc" class="readonly" rows="5" cols="40" placeholder="Job Description..." readonly></textarea><br><br>
  <textarea id="userDesc" rows="5" cols="40" placeholder="Describe yourself..."></textarea><br><br>
  <input type="text" id="fullName" placeholder="Full Name"><br><br>
  <input type="text" id="address" placeholder="Address"><br><br>
  <input type="email" id="email" placeholder="Email"><br><br>
  <input type="tel" id="phoneNumber" placeholder="Phone Number"><br><br>
  <button id="submitBtn">Submit</button>
  <button id="closeBtn" style="background: red; color: white; padding: 5px 10px; border: none; cursor: pointer;">Close</button>
`;

// Append to popup
popup.appendChild(content);

// Functions to open popup with selected text
function showPopupWithSelectedText(selectedText) {
  if (popup.parentNode) {
    popup.remove();
  }
  document.body.appendChild(popup);
  document.getElementById('jobDesc').value = selectedText || "No job description selected.";
}

// Button behaviors
content.querySelector('#closeBtn').onclick = () => popup.remove();

content.querySelector('#submitBtn').onclick = () => {
  const jobDesc = document.getElementById('jobDesc').value;
  const userDesc = document.getElementById('userDesc').value;
  const fullName = document.getElementById('fullName').value;
  const address = document.getElementById('address').value;
  const email = document.getElementById('email').value;
  const phoneNumber = document.getElementById('phoneNumber').value;
  popup.remove();
  fetch(`http://localhost:4567/write?jobDesc=${encodeURIComponent(jobDesc)}&userDesc=${encodeURIComponent(userDesc)}&fullName=${encodeURIComponent(fullName)}&address=${encodeURIComponent(address)}&email=${encodeURIComponent(email)}&phoneNumber=${encodeURIComponent(phoneNumber)}`)
      .then(res => res.blob())
      .then(blob => {
			
		  const link = document.createElement('a');
          const url = URL.createObjectURL(blob);
          link.href = url;
          link.download = 'coverLetter.docx'; 
          document.body.appendChild(link);
          link.click();
          document.body.removeChild(link);

          alert('Success! File downloaded.');
          
      })
      .catch(err => {
          console.error('Fetch error:', err);
      });
};

