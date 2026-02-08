import { useState } from 'react';
import { addCategory } from './api';

const CategoryManager = ({ onCategoryAdded, existingCategories }) => {
  const [catName, setCatName] = useState('');
  const [catType, setCatType] = useState('EXPENSE');

  const handleAddCategory = async (e) => {
    e.preventDefault();
    try {
      // Matches your CategoryRequest DTO
      const response = await addCategory({ name: catName, type: catType });
      setCatName(''); // Clear input
      onCategoryAdded(response.data); // Tell the parent to refresh the list
      alert("Category Created!");
    } catch (err) {
      alert("Error: " + (err.response?.data?.message || "Failed to create category"));
    }
  };

 return (
  <div className="bg-neutral-900 border border-neutral-800 rounded-xl p-6 mb-8">
    <h3 className="text-lg font-semibold text-white mb-4 flex items-center gap-2">
      <span className="text-indigo-400">📁</span> Manage Categories
    </h3>
    <form onSubmit={handleAddCategory} className="flex flex-wrap gap-3 mb-6">
      <input type="text" placeholder="e.g., Groceries" value={catName} onChange={(e) => setCatName(e.target.value)} required 
        className="flex-1 min-w-[200px] bg-neutral-800 border border-neutral-700 text-white px-4 py-2 rounded-lg outline-none focus:border-indigo-500" />
      <select value={catType} onChange={(e) => setCatType(e.target.value)}
        className="bg-neutral-800 border border-neutral-700 text-white px-4 py-2 rounded-lg outline-none">
        <option value="EXPENSE">Expense</option>
        <option value="INCOME">Income</option>
      </select>
      <button type="submit" className="bg-indigo-600 hover:bg-indigo-500 text-white px-6 py-2 rounded-lg font-medium transition">Create</button>
    </form>

    <div className="flex flex-wrap gap-2">
      {existingCategories.map(cat => (
        <span key={cat.id} className="px-3 py-1 bg-neutral-800 border border-neutral-700 text-neutral-300 text-xs rounded-full flex items-center gap-2">
          <span className={cat.type === 'INCOME' ? 'text-green-500' : 'text-red-500'}>●</span>
          {cat.name}
        </span>
      ))}
    </div>
  </div>
);
};

export default CategoryManager;