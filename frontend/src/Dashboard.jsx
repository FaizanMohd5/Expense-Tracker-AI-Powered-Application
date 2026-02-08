import { useState, useEffect } from 'react';
// Added deleteExpense and getProfile to your imports
import { getExpenses, addExpense, getCategories, deleteExpense, getProfile } from './api';
import CategoryManager from './CategoryManager';

const Dashboard = () => {
    const [expenses, setExpenses] = useState([]);
    const [categories, setCategories] = useState([]);
    const [newExpense, setNewExpense] = useState({
        amount: '', categoryId: '', type: 'EXPENSE', paymentMethod: 'CASH', date: new Date().toISOString().split('T')[0], note: ''
    });
    const [currency, setCurrency] = useState('INR'); 

    useEffect(() => {
        const fetchProfile = async () => {
            try {
                const res = await getProfile();
                // Ensure your backend returns 'currency' in the response
                if (res.data.currency) setCurrency(res.data.currency);
            } catch (err) { 
                console.error("Could not fetch currency, using default INR"); 
            }
        };
        fetchProfile();
        loadData();
    }, []);

    const loadData = async () => {
        try {
            const [expenseRes, catRes] = await Promise.all([getExpenses(), getCategories()]);
            setExpenses(expenseRes.data);
            setCategories(catRes.data);
        } catch (err) {
            console.error("Failed to load dashboard data", err);
        }
    };

    const onCategoryAdded = (newCat) => {
        setCategories(prev => [...prev, newCat]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await addExpense(newExpense);
            loadData();
            setNewExpense({ ...newExpense, amount: '', note: '' }); // Clear form fields
            alert("Expense Added!");
        } catch (err) {
            alert("Error adding expense");
        }
    };

    const handleDelete = async (id) => {
        if (window.confirm("Are you sure you want to delete this?")) {
            try {
                await deleteExpense(id);
                loadData();
            } catch (err) {
                alert("Failed to delete expense");
            }
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        window.location.reload();
    };

    return (
        <div className="min-h-screen bg-black text-neutral-200 p-4 md:p-8">
            <div className="max-w-6xl mx-auto">
                {/* Top Header */}
                <header className="flex justify-between items-center mb-10">
                    <div>
                        <h1 className="text-4xl font-bold text-white tracking-tight">Dashboard</h1>
                        <p className="text-neutral-500">Track your daily flow</p>
                    </div>
                    <button onClick={handleLogout} className="bg-neutral-900 hover:bg-red-900/20 text-red-500 border border-red-900/50 px-5 py-2 rounded-full transition-all text-sm font-medium">
                        Logout
                    </button>
                </header>

                <CategoryManager onCategoryAdded={onCategoryAdded} existingCategories={categories} />

                {/* Add Expense Form */}
                <div className="bg-neutral-900/50 border border-neutral-800 p-6 rounded-2xl mb-8">
                    <form onSubmit={handleSubmit} className="grid grid-cols-1 md:grid-cols-5 gap-4">
                        <input type="number" placeholder="Amount" value={newExpense.amount} required 
                            onChange={(e) => setNewExpense({ ...newExpense, amount: e.target.value })}
                            className="bg-neutral-800 border border-neutral-700 p-3 rounded-xl outline-none focus:border-indigo-500" />
                        
                        <select required value={newExpense.categoryId} onChange={(e) => setNewExpense({ ...newExpense, categoryId: e.target.value })} 
                            className="bg-neutral-800 border border-neutral-700 p-3 rounded-xl outline-none">
                            <option value="">Category</option>
                            {categories.map(cat => <option key={cat.id} value={cat.id}>{cat.name}</option>)}
                        </select>

                        <select value={newExpense.type} onChange={(e) => setNewExpense({ ...newExpense, type: e.target.value })} 
                            className="bg-neutral-800 border border-neutral-700 p-3 rounded-xl outline-none">
                            <option value="EXPENSE">Expense</option>
                            <option value="INCOME">Income</option>
                        </select>

                        <input type="date" value={newExpense.date} onChange={(e) => setNewExpense({ ...newExpense, date: e.target.value })}
                            className="bg-neutral-800 border border-neutral-700 p-3 rounded-xl outline-none" />
                        
                        <button type="submit" className="bg-white text-black font-bold py-3 rounded-xl hover:bg-neutral-200 transition active:scale-95">
                            Add Transaction
                        </button>
                    </form>
                </div>

                {/* Modern Table */}
                <div className="overflow-x-auto bg-neutral-900 border border-neutral-800 rounded-2xl shadow-xl">
                    <table className="w-full text-left border-collapse">
                        <thead>
                            <tr className="bg-neutral-800/50">
                                <th className="p-4 text-neutral-400 font-medium uppercase text-xs">Date</th>
                                <th className="p-4 text-neutral-400 font-medium uppercase text-xs">Category</th>
                                <th className="p-4 text-neutral-400 font-medium uppercase text-xs">Type</th>
                                <th className="p-4 text-neutral-400 font-medium uppercase text-xs text-right">Amount</th>
                                <th className="p-4 text-neutral-400 font-medium uppercase text-xs text-center">Action</th>
                            </tr>
                        </thead>
                        <tbody className="divide-y divide-neutral-800">
                            {expenses.map(exp => (
                                <tr key={exp.id} className="hover:bg-neutral-800/30 transition-colors">
                                    <td className="p-4 text-sm">{exp.date}</td>
                                    <td className="p-4 text-sm font-medium">{categories.find(c => c.id === exp.categoryId)?.name || 'General'}</td>
                                    <td className="p-4 text-xs font-bold">
                                        <span className={exp.type === 'INCOME' ? 'text-green-400' : 'text-red-400'}>{exp.type}</span>
                                    </td>
                                    <td className={`p-4 text-right font-mono ${exp.type === 'INCOME' ? 'text-green-400' : 'text-white'}`}>
                                        {exp.type === 'INCOME' ? '+' : '-'} {exp.amount} 
                                        <span className="ml-1 text-[10px] text-neutral-500 uppercase">{currency}</span>
                                    </td>
                                    <td className="p-4 text-center">
                                        <button onClick={() => handleDelete(exp.id)} className="text-neutral-600 hover:text-red-500 transition px-2">
                                            Remove
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};

export default Dashboard;