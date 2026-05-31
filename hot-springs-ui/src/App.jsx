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

  // newSoakerName - holds the value typed into the name input field
  const [newSoakerName, setNewSoakerName] = useState('')

  // newSoakerEmail - holds the value typed into the email input field
  const [newSoakerEmail, setNewSoakerEmail] = useState('')

  // newHotSpringName - holds the value typed into the hot spring name field
  const [newHotSpringName, setNewHotSpringName] = useState('')

  // newHotSpringCounty - holds the value typed into the county field
  const [newHotSpringCounty, setNewHotSpringCounty] = useState('')

  // newHotSpringDirections - holds the value typed into the directions field
  const [newHotSpringDirections, setNewHotSpringDirections] = useState('')

  // newHotSpringDetails - holds the selected details for the new hot spring
  const [newHotSpringDetails, setNewHotSpringDetails] = useState([])

  // allDetails - holds all available details fetched from the API
  const [allDetails, setAllDetails] = useState([])


  // CONSTANTS
  // The base URL of my Spring Boot API
  const API_URL = 'http://localhost:8080/hot_spring'

  // EFFECTS
  // useEffects runs when the component first loads
  // The empty [] means it only runs once, not on every re-render
  useEffect(() => {
    fetchSoakers()
    fetchAllDetails()
  }, [])

  // FUNCTIONS
  // fetchSoakers - calls my API and gets all soakerss
  const fetchSoakers = async () => {
    const response = await fetch(`${API_URL}/soaker`)
    const data = await response.json()
    setSoakers(data)
  }

  // fetchHotSprings - calls our API and gets all hot springs for a specific soaker
  // soakerId - the ID of the soaker whose hot springs I want
  const fetchHotSprings = async (soakerId) => {
    const response = await fetch(`${API_URL}/soaker/${soakerId}/hot_spring`)
    const data = await response.json()
    setSelectedSoaker(prev => ({ ...prev, hotSprings: data}))
  }

  //fetchAllDetails - gets all available detail tags from the API
  const fetchAllDetails = async () => {
    const response = await fetch(`${API_URL}/detail`)
    const data = await response.json()
    setAllDetails(data)
  }

  // handleAddSoaker - send a POST request to create a new soaker
  // prevents the page from refreshing when the form is submitted
  const handleAddSoaker = async (e) => {
    e.preventDefault()

    // send POST request to the API with the new soaker data
    await fetch(`${API_URL}/soaker`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        soakerName: newSoakerName,
        soakerEmail: newSoakerEmail
      })
    })

    //clear the form fields after submitting
    setNewSoakerName('')
    setNewSoakerEmail('')

    // refresh the soakers list so the new soaker appears
    fetchSoakers()
  }

  // handleAddHotSpring - sends a POST request to create a new hot spring
  // linked to the currently selected soaker
  const handleAddHotSpring = async (e) => {
    e.preventDefault()

    // send POST request to the API with the new hot spring data
    await fetch(`${API_URL}/soaker/${selectedSoaker.soakerId}/hot_spring`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        hotSpringName: newHotSpringName,
        county: newHotSpringCounty,
        directions: newHotSpringDirections,
        details: newHotSpringDetails
      })
    })

    // clear the form fields after submitting
    setNewHotSpringName('')
    setNewHotSpringCounty('')
    setNewHotSpringDirections('')
    setNewHotSpringDetails([])

    // refresh the hot springs list so the new one appears
    fetchHotSprings(selectedSoaker.soakerId)
  }

  //RENDER
  // This is the JSX that gets displayed in the browser
  return (
    <div className='app'>
      <h1>HotSprings App</h1>
      <p>Community-driven hot spring condition reports for Idaho - the state with the most hotsprings in the country</p>

      {/* Form to add a new soaker */}
      <h2>Add a Soaker</h2>
      <form onSubmit={handleAddSoaker}>
        <div>
          {/* name input - updates newSoakerName state as user types */}
          <label>Name:</label>
          <input
            type="text"
            value={newSoakerName}
            onChange={(e) => setNewSoakerName(e.target.value)}
            placeholder="Enter name"
            required
          />
        </div>
        <div>
          {/* email input - updates newSoakerEmail state as user types */}
          <label>Email:</label>
          <input
            type="email"
            value={newSoakerEmail}
            onChange={(e) => setNewSoakerEmail(e.target.value)}
            placeholder="Enter email"
            required
          />
        </div>
        {/* submit button - triggers handleAddSoaker */}
        <button type="submit">Add Soaker</button>
      </form>
      {/* List of all soakers */}
      <h2>Soakers</h2>
      <ul>
        {soakers.map(soaker => (
          <li 
            key={soaker.soakerId}
            onClick={() => {
              // when clicked, set this soaker as selected and fetch their hot springs
              setSelectedSoaker(soaker)
              fetchHotSprings(soaker.soakerId)
            }}
            style={{ cursor: 'pointer'}}
          >
            {soaker.soakerName} - {soaker.soakerEmail}
          </li>
        ))}
      </ul>
      {/* Show selected soaker's hot springs when a soaker is clicked */}
      {selectedSoaker && (
        <div>
          {/* back button - clears selectedSoaker so the user returns to the full list */}
          <button onClick={() => setSelectedSoaker(null)}>Back to Soakers</button>
          <h2>{selectedSoaker.soakerName}'s Hot Springs</h2>
          
          {/* Form to add a new hot spring for the selected soaker */}
          <h3>Add a Hot Spring</h3>
          <form onSubmit={handleAddHotSpring}>
            <div>
              <label>Name:</label>
              <input
                type="text"
                value={newHotSpringName}
                onChange={(e) => setNewHotSpringName(e.target.value)}
                placeholder="Enter hot spring name"
                required
              />
            </div>
            <div>
              <label>County:</label>
              <input
              type="text"
              value={newHotSpringCounty}
              onChange={(e) => setNewHotSpringCounty(e.target.value)}
              placeholder="Enter county"
              required
              />
            </div>
            <div>
              <label>Directions:</label>
              <textarea
              value={newHotSpringDirections}
              onChange={(e) => setNewHotSpringDirections(e.target.value)}
              placeholder="Enter directions"
              required
              />
            </div>
            <div>
              <label>Details:</label>
              {/* checkboxes for each available detail tag */}
              {allDetails.map((detail, index) => (
                <div key={index}>
                  <input
                    type="checkbox"
                    id={index}
                    value={detail}
                    onChange={(e) => {
                      if (e.target.checked) {
                        setNewHotSpringDetails(prev => [...prev, detail])
                      } else {
                        setNewHotSpringDetails(prev =>
                          prev.filter(d => d !== detail))
                      }
                    }}
                  />
                  <label htmlFor={index}>{detail}</label>
                </div>
              ))}
            </div>
            <button type="submit">Add Hot Spring</button>
          </form>
          <ul>
            {selectedSoaker.hotSprings && selectedSoaker.hotSprings.map(hotSpring => (
              <li key={hotSpring.hotSpringId}>
                <strong>{hotSpring.hotSpringName}</strong>
                <p>County: {hotSpring.county}</p>
                <p>Directions: {hotSpring.directions}</p>
                <p>Details: {hotSpring.details.join(', ')}</p>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  )
}

// Export the App component so main.jsx can use it
export default App

