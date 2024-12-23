//================= Tabs da Página
document.addEventListener('DOMContentLoaded', function () {
    // Get all tab buttons
    const tabButtons = document.querySelectorAll('[data-bs-toggle="tab"]');
    
    // Function to activate a tab
    function activateTab(tabButton) {
        // Remove active and custom class from all tab buttons
        tabButtons.forEach((btn) => {
            btn.classList.remove('active', 'custom-active');
            btn.style.backgroundColor = ''; // Reset background color
            btn.style.color = 'black'; // Reset text color
        });
        
        // Set the clicked tab button as active and add custom class
        tabButton.classList.add('active', 'custom-active');
        tabButton.style.backgroundColor = 'black'; // Active background color
        tabButton.style.color = 'white'; // Active text color
        
        // Get the target tab pane
        const targetTabPaneId = tabButton.getAttribute('data-bs-target');
        const targetTabPane = document.querySelector(targetTabPaneId);
        
        // Hide all tab panes
        const allTabPanes = document.querySelectorAll('.tab-pane');
        allTabPanes.forEach((pane) => pane.classList.remove('show', 'active'));
        
        // Show the target tab pane
        targetTabPane.classList.add('show', 'active');
    }

    // Activate the default tab when the page loads
    const defaultTabButton = document.querySelector('.nav-link.active');
    if (defaultTabButton) {
        activateTab(defaultTabButton);
    }
    
    // Loop through each tab button to add a click event listener
    tabButtons.forEach((tabButton) => {
        tabButton.addEventListener('click', function (event) {
            event.preventDefault();
            activateTab(this);
        });
    });
});

//================== Dropdown para selecionar a empresa
document.addEventListener('DOMContentLoaded', function () {
    // Function to handle dropdown selection for companies (in the Company and Employee tabs)
    function handleDropdownSelection(dropdownClass, endpoint, updateFieldsCallback) {
        const dropdownItems = document.querySelectorAll(`${dropdownClass} .dropdown-item`);

        dropdownItems.forEach((item) => {
            item.addEventListener('click', function (event) {
                event.preventDefault();

                const selectedItemName = this.textContent;
                const selectedCompanyId = this.getAttribute('data-company');

                // Update the dropdown button text
                const dropdownButton = this.closest(dropdownClass).querySelector('.dropdown-toggle');
                dropdownButton.textContent = selectedItemName;

                // Save selected company in localStorage
                localStorage.setItem('selectedCompany', selectedCompanyId);

                // Send the AJAX request to fetch data (either company or employees)
                fetch(endpoint, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({ company_id: selectedCompanyId })
                })
                .then(response => response.json())
                .then(data => {
                    if (data.error) {
                        console.error(data.error);
                        return;
                    }
                    // Debugging: Log employee data to ensure it includes employee_id
                    //console.log(data.employees);

                    // Call the callback function to update the specific fields in the UI
                    updateFieldsCallback(data);
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
            });
        });
    }

    // Callback to update the fields in the company tab
    function updateCompanyFields(data) {
        document.getElementById('companyNameField').textContent = data.company_name;
        document.getElementById('companyWebsiteField').textContent = data.website;
        document.getElementById('companyJobPositionField').textContent = data.job_position;
        document.getElementById('companyDescriptionField').textContent = data.description;
    }

    // Callback to update the employees list in the employee tab with edit and delete buttons
    function updateEmployeeFields(data) {
        const employeeTableBody = document.querySelector('#ticketsTable tbody');
        employeeTableBody.innerHTML = '';  // Clear any existing content

        if (data.employees.length === 0) {
            employeeTableBody.innerHTML = '<tr><td colspan="4">No employees found for this company.</td></tr>';
        } else {
            data.employees.forEach(employee => {
                const row = document.createElement('tr');

                // Create table cells (td) for each employee field
                const nameCell = document.createElement('td');
                nameCell.textContent = employee.name;
                row.appendChild(nameCell);

                const positionCell = document.createElement('td');
                positionCell.textContent = employee.position;
                row.appendChild(positionCell);

                const departmentCell = document.createElement('td');
                departmentCell.textContent = employee.department || 'N/A';  // Use 'N/A' if no department is provided
                row.appendChild(departmentCell);

                const notesCell = document.createElement('td');
                notesCell.textContent = employee.notes || 'No notes';
                row.appendChild(notesCell);

                // Create actions cell with Edit and Delete buttons
                const actionsCell = document.createElement('td');
                
                // Add Edit Employee Button
                const editButton = `
                    <button type="button" style="color: transparent; background-color: transparent; border: none;" data-toggle="modal" data-target="#editEmployeeModal-${employee.employee_id}">
                        <i class="fas fa-edit" style="color: blue;"></i>
                    </button>
                `;
                
                // Add Delete Employee Button
                const deleteButton = `
                    <form class="delete-employee-form" data-employee-id="${employee.employee_id}" style="display: inline;">
                        <button type="button" class="delete-employee-btn" style="color: transparent; background-color: transparent; border: none;">
                            <i class="far fa-trash-alt" style="color: red;"></i>
                        </button>
                    </form>
                `;

                // Append buttons to the actions cell
                actionsCell.innerHTML = editButton + deleteButton;
                row.appendChild(actionsCell);

                // Append the row to the table body
                employeeTableBody.appendChild(row);
            });

            // Add event listeners to the delete buttons
            const deleteButtons = document.querySelectorAll('.delete-employee-btn');
            deleteButtons.forEach(button => {
                button.addEventListener('click', function (event) {
                    const employeeId = this.closest('.delete-employee-form').getAttribute('data-employee-id');
                    if (confirm('Are you sure you want to delete this employee?')) {
                        deleteEmployee(employeeId);
                    }
                });
            });

        }
    }

    // Function to delete employee via AJAX
    function deleteEmployee(employeeId) {
        fetch(`/delete_employee/${employeeId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                // Refresh employee list after deletion
                alert(data.message);
                // Save the tab state before reloading
                localStorage.setItem('activeTab', '#nav-employee');
                // Refresh employee list after deletion
                location.reload();
            } else {
                alert('Error: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error deleting employee:', error);
        });
    }

    // Restore the selected company and tab after page reload
    function restoreState() {
        const activeTab = localStorage.getItem('activeTab');
        const selectedCompany = localStorage.getItem('selectedCompany');

        // Restore active tab
        if (activeTab) {
            const tabToActivate = document.querySelector(activeTab);
            const tabButton = document.querySelector(`[data-bs-target="${activeTab}"]`);
            if (tabButton) {
                tabButton.classList.add('active');
            }
            if (tabToActivate) {
                tabToActivate.classList.add('show', 'active');
            }
            // Clear the saved tab state after restoring
            localStorage.removeItem('activeTab');
        }

        // Restore selected company in the dropdown
        if (selectedCompany) {
            const dropdownButton = document.querySelector('.tab-pane#nav-employee .dropdown-toggle');
            const selectedDropdownItem = document.querySelector(`.tab-pane#nav-employee .dropdown-item[data-company="${selectedCompany}"]`);

            if (dropdownButton && selectedDropdownItem) {
                dropdownButton.textContent = selectedDropdownItem.textContent;
                // Optionally trigger loading of employees
                selectedDropdownItem.click();
            }
        }
    }

    // Apply the function for the company dropdown
    handleDropdownSelection('.tab-pane#nav-company', '/dashboard/configuration', updateCompanyFields);

    // Apply the function for the employee dropdown
    handleDropdownSelection('.tab-pane#nav-employee', '/dashboard/configuration/employees', updateEmployeeFields);

    // Function to confirm delete employee action
    window.confirmDeleteEmployee = function () {
        return confirm('Are you sure you want to delete this employee?');
    }

    // Restore the previous state on page load
    restoreState();
    // Clear all localStorage items 
    localStorage.clear();
});