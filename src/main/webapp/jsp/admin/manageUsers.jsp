<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, com.dental.model.User" %>
<%
    if (session.getAttribute("username") == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    String role = (String) session.getAttribute("role");
    if (!"admin".equals(role)) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Users - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .container {
            max-width: 1100px;
            margin: 30px auto;
            background: white;
            padding: 35px 40px;
            border-radius: 12px;
            box-shadow: 0 2px 20px rgba(0,0,0,0.1);
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 2px solid #e0e0e0;
            padding-bottom: 15px;
            margin-bottom: 25px;
            flex-wrap: wrap;
        }
        .header h2 {
            color: #2c3e50;
        }
        .btn-add {
            background: #27ae60;
            color: white;
            padding: 10px 25px;
            border: none;
            border-radius: 8px;
            font-weight: bold;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }
        .btn-add:hover {
            background: #219a52;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 15px;
            font-size: 14px;
        }
        table th {
            background: #2c3e50;
            color: white;
            padding: 12px 15px;
            text-align: left;
        }
        table td {
            padding: 10px 15px;
            border-bottom: 1px solid #e5e7eb;
        }
        table tr:hover {
            background: #f9fafb;
        }
        .status-badge {
            padding: 3px 12px;
            border-radius: 20px;
            font-size: 11px;
            font-weight: 600;
        }
        .status-active { background: #d1fae5; color: #065f46; }
        .status-inactive { background: #fee2e2; color: #991b1b; }
        .role-badge {
            padding: 3px 12px;
            border-radius: 20px;
            font-size: 11px;
            font-weight: 600;
        }
        .role-admin { background: #fef3c7; color: #92400e; }
        .role-staff { background: #dbeafe; color: #2563eb; }
        .btn-action {
            padding: 5px 12px;
            border: none;
            border-radius: 5px;
            font-size: 12px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            margin: 0 2px;
        }
        .btn-edit {
            background: #3498db;
            color: white;
        }
        .btn-edit:hover {
            background: #2980b9;
        }
        .btn-deactivate {
            background: #f39c12;
            color: white;
        }
        .btn-deactivate:hover {
            background: #e67e22;
        }
        .btn-activate {
            background: #27ae60;
            color: white;
        }
        .btn-activate:hover {
            background: #219a52;
        }
        .btn-delete {
            background: #e74c3c;
            color: white;
        }
        .btn-delete:hover {
            background: #c0392b;
        }
        .btn-back {
            background: #95a5a6;
            color: white;
            padding: 10px 25px;
            border: none;
            border-radius: 8px;
            text-decoration: none;
            display: inline-block;
            margin-top: 20px;
        }
        .btn-back:hover { background: #7f8c8d; }
        .modal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0,0,0,0.5);
            z-index: 1000;
            justify-content: center;
            align-items: center;
        }
        .modal.active {
            display: flex;
        }
        .modal-content {
            background: white;
            padding: 30px;
            border-radius: 12px;
            max-width: 500px;
            width: 90%;
            max-height: 80vh;
            overflow-y: auto;
        }
        .modal-content h3 {
            border-bottom: 2px solid #667eea;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }
        .modal-content .form-group {
            margin-bottom: 15px;
        }
        .modal-content .form-group label {
            display: block;
            font-weight: 600;
            margin-bottom: 5px;
            font-size: 13px;
        }
        .modal-content .form-group input,
        .modal-content .form-group select {
            width: 100%;
            padding: 8px 12px;
            border: 2px solid #e0e0e0;
            border-radius: 6px;
            font-size: 14px;
            box-sizing: border-box;
        }
        .modal-content .form-group input:focus,
        .modal-content .form-group select:focus {
            border-color: #667eea;
            outline: none;
        }
        .modal-content .form-group .checkbox-label {
            display: flex;
            align-items: center;
            gap: 10px;
            font-weight: normal;
            cursor: pointer;
        }
        .modal-content .form-group .checkbox-label input {
            width: auto;
        }
        .modal-actions {
            display: flex;
            gap: 10px;
            margin-top: 20px;
        }
        .modal-actions button {
            padding: 10px 25px;
            border: none;
            border-radius: 6px;
            font-weight: bold;
            cursor: pointer;
        }
        .btn-save {
            background: #27ae60;
            color: white;
        }
        .btn-save:hover {
            background: #219a52;
        }
        .btn-cancel {
            background: #95a5a6;
            color: white;
        }
        .btn-cancel:hover {
            background: #7f8c8d;
        }
        .success-msg {
            background: #d5f5e3;
            color: #27ae60;
            padding: 15px;
            border-radius: 8px;
            border-left: 4px solid #27ae60;
            margin-bottom: 20px;
        }
        .error-msg {
            background: #fdebd0;
            color: #e67e22;
            padding: 15px;
            border-radius: 8px;
            border-left: 4px solid #e67e22;
            margin-bottom: 20px;
        }
        @media (max-width: 600px) {
            .container { padding: 20px; }
            table { font-size: 12px; }
            table th, table td { padding: 8px 10px; }
            .header { flex-direction: column; align-items: stretch; gap: 15px; }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h2>👥 Manage Users</h2>
            <button class="btn-add" onclick="openAddModal()">➕ Add New User</button>
        </div>
        
        <% if (request.getAttribute("success") != null) { %>
            <div class="success-msg">✅ <%= request.getAttribute("success") %></div>
        <% } %>
        
        <% if (request.getAttribute("error") != null) { %>
            <div class="error-msg">⚠️ <%= request.getAttribute("error") %></div>
        <% } %>
        
        <%
            List<User> users = (List<User>) request.getAttribute("users");
            if (users != null && !users.isEmpty()) {
        %>
            <div style="overflow-x: auto;">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>Full Name</th>
                            <th>Email</th>
                            <th>Role</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (User user : users) { %>
                            <tr>
                                <td><%= user.getUserId() %></td>
                                <td><strong><%= user.getUsername() %></strong></td>
                                <td><%= user.getFullName() %></td>
                                <td><%= user.getEmail() != null ? user.getEmail() : "-" %></td>
                                <td>
                                    <span class="role-badge role-<%= user.getRole() %>">
                                        <%= user.getRole() != null ? user.getRole().toUpperCase() : "STAFF" %>
                                    </span>
                                </td>
                                <td>
                                    <span class="status-badge <%= user.isActive() ? "status-active" : "status-inactive" %>">
                                        <%= user.isActive() ? "Active" : "Inactive" %>
                                    </span>
                                </td>
                                <td>
                                    <button class="btn-action btn-edit" onclick="editUser('<%= user.getUserId() %>', '<%= user.getFullName() %>', '<%= user.getEmail() != null ? user.getEmail() : "" %>', '<%= user.getRole() %>', <%= user.isActive() %>)">
                                        ✏️ Edit
                                    </button>
                                    <% if (user.isActive()) { %>
                                        <button class="btn-action btn-deactivate" onclick="confirmDeactivate('<%= user.getUserId() %>')">
                                            ⛔ Deactivate
                                        </button>
                                    <% } else { %>
                                        <button class="btn-action btn-activate" onclick="confirmActivate('<%= user.getUserId() %>')">
                                            ✅ Activate
                                        </button>
                                    <% } %>
                                    <% if (!"admin".equals(user.getRole())) { %>
                                        <button class="btn-action btn-delete" onclick="confirmDelete('<%= user.getUserId() %>')">
                                            🗑️ Delete
                                        </button>
                                    <% } %>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        <%
            } else {
        %>
            <div style="text-align: center; padding: 40px; color: #6b7280;">
                <span style="font-size: 48px; display: block;">👤</span>
                <p>No users found.</p>
            </div>
        <%
            }
        %>
        
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn-back">← Back to Dashboard</a>
    </div>
    
    <!-- Add User Modal -->
    <div id="addModal" class="modal">
        <div class="modal-content">
            <h3>➕ Add New User</h3>
            <form action="${pageContext.request.contextPath}/admin/users" method="post">
                <input type="hidden" name="action" value="add">
                <div class="form-group">
                    <label>Username *</label>
                    <input type="text" name="username" required>
                </div>
                <div class="form-group">
                    <label>Password *</label>
                    <input type="text" name="password" required>
                </div>
                <div class="form-group">
                    <label>Full Name *</label>
                    <input type="text" name="fullName" required>
                </div>
                <div class="form-group">
                    <label>Email</label>
                    <input type="email" name="email">
                </div>
                <div class="form-group">
                    <label>Role</label>
                    <select name="role">
                        <option value="staff">Staff</option>
                        <option value="admin">Admin</option>
                    </select>
                </div>
                <div class="form-group">
                    <label class="checkbox-label">
                        <input type="checkbox" name="isActive" checked> Active
                    </label>
                </div>
                <div class="modal-actions">
                    <button type="submit" class="btn-save">Save</button>
                    <button type="button" class="btn-cancel" onclick="closeModal('addModal')">Cancel</button>
                </div>
            </form>
        </div>
    </div>
    
    <!-- Edit User Modal -->
    <div id="editModal" class="modal">
        <div class="modal-content">
            <h3>✏️ Edit User</h3>
            <form action="${pageContext.request.contextPath}/admin/users" method="post">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="userId" id="editUserId">
                <div class="form-group">
                    <label>Full Name *</label>
                    <input type="text" name="fullName" id="editFullName" required>
                </div>
                <div class="form-group">
                    <label>Email</label>
                    <input type="email" name="email" id="editEmail">
                </div>
                <div class="form-group">
                    <label>Role</label>
                    <select name="role" id="editRole">
                        <option value="staff">Staff</option>
                        <option value="admin">Admin</option>
                    </select>
                </div>
                <div class="form-group">
                    <label class="checkbox-label">
                        <input type="checkbox" name="isActive" id="editIsActive"> Active
                    </label>
                </div>
                <div class="modal-actions">
                    <button type="submit" class="btn-save">Update</button>
                    <button type="button" class="btn-cancel" onclick="closeModal('editModal')">Cancel</button>
                </div>
            </form>
        </div>
    </div>
    
    <script>
        // Open Add Modal
        function openAddModal() {
            document.getElementById('addModal').classList.add('active');
        }
        
        // Close Modal
        function closeModal(modalId) {
            document.getElementById(modalId).classList.remove('active');
        }
        
        // Edit User - Pass data directly from JSP
        function editUser(userId, fullName, email, role, isActive) {
            document.getElementById('editUserId').value = userId;
            document.getElementById('editFullName').value = fullName || '';
            document.getElementById('editEmail').value = email || '';
            document.getElementById('editRole').value = role || 'staff';
            document.getElementById('editIsActive').checked = isActive === true || isActive === 'true';
            document.getElementById('editModal').classList.add('active');
        }
        
        // Confirm Delete
        function confirmDelete(userId) {
            if (confirm('Are you sure you want to delete this user? This action cannot be undone.')) {
                window.location.href = '${pageContext.request.contextPath}/admin/users?action=delete&id=' + userId;
            }
        }
        
        // Confirm Deactivate
        function confirmDeactivate(userId) {
            if (confirm('Are you sure you want to deactivate this user?')) {
                window.location.href = '${pageContext.request.contextPath}/admin/users?action=deactivate&id=' + userId;
            }
        }
        
        // Confirm Activate
        function confirmActivate(userId) {
            window.location.href = '${pageContext.request.contextPath}/admin/users?action=activate&id=' + userId;
        }
    </script>
</body>
</html>