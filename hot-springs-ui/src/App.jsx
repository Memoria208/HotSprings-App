// App.jsx - The root component of the HotSprings app
// My first time using React - learning experience
// This is the main container that holds all other components
// and handles routing between different pages

// Import React hooks I'll need
// useState - lets me store and update data in a component
// useEffect - lets me run code when a component loads (like fetching data)
import { useState, useEffect } from "react"

// Import my CSS styles
import './App.css'

// The main App component - this is what gets rendered in the browser
function App() {

  // STATE
  // soakers - the list of all soakers fetched from my API
  // setSoakers - the function I call to update the soakers list
  const [soakers, setSoakers] = useState([])

  // selectedSoaker - the soaker the user has clicked on to view their hot springs
  // setSelectedSoaker - the function we call to update the selected soaker
  // starts as null because nothing is selected when the app first loads
  const [selectedSoaker, setSelectedSoaker] = useState(null)

  // CONSTANTS
  // The base URL of my Spring Boot API
  const API_URL = 'http://localhost:8080/hot_spring'

  // EFFECTS
  // useEffects runs when the component first loads
  // The empty [] means it only runs once, not on every re-render
  useEffect(() => {
    fetchSoakers()
  }, [])

  // FUNCTIONS
  // fetchSoakers - calls my API and gets all soakerss
  const fetchSoakers = async () => {
    const response = await fetch(`${API_URL}/soaker`)
    const data = await response.json()
    setSoakers(data)
  }

  //RENDER
  // This is the JSX that gets displayed in the browser
  return (
    <div className='app'>
      <h1>HotSprings App</h1>
      <p>Community-driven hot spring condition reports for Idaho - the state with the most hotsprings in the country</p>

      {/* List of all soakers */}
      <h2>Soakers</h2>
      <ul>
        {soakers.map(soaker => (
          <li key={soaker.soakerId}>
            {soaker.soakerName} - {soaker.soakerEmail}
          </li>
        ))}
      </ul>
    </div>
  )
}

// Export the App component so main.jsx can use it
export default App

