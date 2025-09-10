<template>
  <div class="member-admin">
    <h1>Member</h1>
    <button @click="showAddForm = true">Add Member</button>
    <table>
      <thead>
        <tr>
          <th>ID</th>
          <th>nick</th>
          <th>email</th>
          <th>…</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="member in members" :key="member.id">
          <td>{{ member.id }}</td>
          <td>{{ member.nick }}</td>
          <td>{{ member.email }}</td>
          <td>
            <button @click="editMember(member)">Edit</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Add/Edit Member Form -->
    <div v-if="showAddForm || editingMember" class="modal">
      <form @submit.prevent="saveMember">
        <label>
          Nick:
          <input v-model="form.nick" required />
        </label>
        <label>
          Email:
          <input v-model="form.email" type="email" />
        </label>
        <button type="submit">Save</button>
        <button type="button" @click="closeForm">Cancel</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const members = ref([])
const showAddForm = ref(false)
const editingMember = ref(null)
const form = ref({ nick: '', email: '' })

const API_BASE = 'http://localhost:3000/admin/members'

async function fetchMembers() {
  const res = await fetch(API_BASE)
  const data = await res.json()
  // Spring Data REST returns _embedded
  members.value = data._embedded?.members || []
}

function editMember(member) {
  editingMember.value = member
  form.value = { nick: member.nick, email: member.email }
  showAddForm.value = false
}

function closeForm() {
  showAddForm.value = false
  editingMember.value = null
  form.value = { nick: '', email: '' }
}

async function saveMember() {
  if (editingMember.value) {
    // Update
    await fetch(`${API_BASE}/${editingMember.value.id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value)
    })
  } else {
    // Create
    await fetch(API_BASE, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form.value)
    })
  }
  closeForm()
  fetchMembers()
}

onMounted(()=>{
  fetchMembers();
});
</script>

<style scoped>
.member-admin {
  padding: 2rem;
}
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1rem;
}
th, td {
  border: 1px solid #ccc;
  padding: 0.5rem;
}
.modal {
  position: fixed;
  top: 20%;
  left: 50%;
  transform: translate(-50%, 0);
  background: #fff;
  padding: 2rem;
  border: 1px solid #ccc;
  box-shadow: 0px 0px 20px black;
  z-index: 100;
}
</style>