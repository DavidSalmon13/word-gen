chrome.runtime.onInstalled.addListener(() => {
  chrome.contextMenus.create({
    id: "generateCoverLetter",
    title: "Generate Cover Letter",
    contexts: ["selection"]
  });
});

chrome.contextMenus.onClicked.addListener((info, tab) => {
  if (info.menuItemId === "generateCoverLetter") {
    // Pass the selected text to content.js
    chrome.scripting.executeScript({
      target: { tabId: tab.id },
      files: ["content.js"]
    });

    chrome.tabs.sendMessage(tab.id, { type: "SHOW_POPUP", selectedText: info.selectionText });
  }
});
