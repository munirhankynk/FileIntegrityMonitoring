import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './pages/Login';
import Home from './pages/Home';
import AgentDetail from './pages/AgentDetail';
import Events from './pages/Events';

function App() {
  return (
    <Router>
        <Routes>
          <Route path="/" element={<Login />} />
          <Route path="/home" element={<Home />} />
          <Route path="/agent/:id" element={<AgentDetail />} />
          <Route path="/events" element={<Events />} />
        </Routes>
    
    </Router>
  )
}

export default App
