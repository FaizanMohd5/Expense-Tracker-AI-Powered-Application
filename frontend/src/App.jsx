import { useState } from 'react';
import api, { login, register } from './api';
import Dashboard from './Dashboard'; // Ensure this file exists!

function App() {
  const [isRegistering, setIsRegistering] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('token'));
  
  // State for all potential fields
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    password: '',
    currency: 'INR',
    monthlyBudget: 0
  });

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      let response;
      if (isRegistering) {
        // Hits POST /auth/register
        response = await register(formData);
      } else {
        // Hits POST /auth/login
        response = await login({ email: formData.email, password: formData.password });
      }

      localStorage.setItem('token', response.data.token);
      setIsLoggedIn(true);
      alert(isRegistering ? "Registration Successful!" : "Login Successful!");
    } catch (err) {
      console.error(err);
      alert("Error: " + (err.response?.data?.message || "Action failed. Check console."));
    }
  };

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  if (isLoggedIn) {
    return <Dashboard />;
  }

  return (
  <div className="min-h-screen bg-neutral-950 flex items-center justify-center p-4">
    <div className="w-full max-w-md bg-neutral-900 border border-neutral-800 rounded-2xl p-8 shadow-2xl">
      <h1 className="text-3xl font-bold text-white mb-2 text-center">
        {isRegistering ? 'Create Account' : 'Welcome Back'}
      </h1>
      <p className="text-neutral-400 text-center mb-8">
        {isRegistering ? 'Start tracking your wealth today' : 'Manage your expenses with ease'}
      </p>

      <form onSubmit={handleSubmit} className="space-y-4">
        {isRegistering && (
          <input name="name" placeholder="Full Name" onChange={handleChange} required 
            className="w-full bg-neutral-800 border border-neutral-700 text-white p-3 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none transition" />
        )}
        <input name="email" type="email" placeholder="Email Address" onChange={handleChange} required 
          className="w-full bg-neutral-800 border border-neutral-700 text-white p-3 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none transition" />
        <input name="password" type="password" placeholder="Password" onChange={handleChange} required 
          className="w-full bg-neutral-800 border border-neutral-700 text-white p-3 rounded-lg focus:ring-2 focus:ring-indigo-500 outline-none transition" />

        {isRegistering && (
          <div className="grid grid-cols-2 gap-4">
            <input name="currency" placeholder="USD" onChange={handleChange} className="bg-neutral-800 border border-neutral-700 text-white p-3 rounded-lg outline-none" />
            <input name="monthlyBudget" type="number" placeholder="Budget" onChange={handleChange} className="bg-neutral-800 border border-neutral-700 text-white p-3 rounded-lg outline-none" />
          </div>
        )}

        <button type="submit" className="w-full bg-indigo-600 hover:bg-indigo-500 text-white font-semibold py-3 rounded-lg transition-all shadow-lg active:scale-95">
          {isRegistering ? 'Sign Up' : 'Sign In'}
        </button>
      </form>

      <button onClick={() => setIsRegistering(!isRegistering)} className="w-full text-neutral-500 text-sm mt-6 hover:text-indigo-400 transition">
        {isRegistering ? 'Already have an account? Sign In' : "Don't have an account? Create one"}
      </button>
    </div>
  </div>
);
}

export default App;