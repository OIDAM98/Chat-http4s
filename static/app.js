const inputUser = document.getElementById("userName")
const inputMessage = document.getElementById("messageToSend")
const submitBtn = document.getElementById("sendMessage")

const chatDiv = document.getElementById("chat")

const fetchMessages = async () => {
  try {
    const messagesJson = await fetch("/api/chat")
    const messages = await messagesJson.json()
    console.log(messages)
    for (message of messages) {
      console.log(message)
      const msg = createDiv(message)
      chatDiv.prepend(msg)
    }
  } catch (e) {
    console.error(e)
  }
}

fetchMessages().catch(e => console.error(e))

const createDiv = message => {
  const div = document.createElement("div")
  div.className = "message card"
  const byP = document.createElement("h5")
  byP.className = "card-header text-white bg-info"
  byP.innerText =  "@" + message.by.value + ":"
  div.appendChild(byP)
  const msgP = document.createElement("p")
  msgP.className = "card-text"
  msgP.innerText = message.txt.value
  div.appendChild(msgP)
  return div
}


submitBtn.addEventListener('click', async () => {
  const author = inputUser.value
  const msg = inputMessage.value
  const json = {
    author,
    msg
  }
  try {
    const resp = await fetch("/api/chat",
      {
        method: "POST",
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(json)
      })

    const message = await resp.json()
    console.log(message)
    const msg = createDiv(message)
    chatDiv.prepend(msg)

  }
  catch (e) {
    console.error(e)
  }
})
