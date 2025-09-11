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
          <th>approved</th>
          <th>…</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="member in members" :key="member.id">
          <td>{{ member.id }}</td>
          <td>{{ member.nick }}</td>
          <td>{{ member.email }}</td>
          <td>{{ member.approved }}</td>
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
          별명:
          <input type="text" v-model="form.nick" required />
        </label>
        <label>
          이메일:
          <input type="email" v-model="form.email" />
        </label>
        <label>
          승인여부:
          <input type="checkbox" v-model="form.approved" />
        </label>
        <button type="submit">Save</button>
        <button type="button" @click="closeForm">Cancel</button>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">

import type { Member } from '@/types/member';

import api from "@/api/axiosInstanceAdmin";

import { ref, onMounted } from 'vue'

const members = ref<Member[]>([])
const showAddForm = ref(false)
const editingMember = ref<Member | null>(null)
const form = ref({ nick: '', email: null as string | null, approved: false })

async function fetchMembers() {
  members.value = (await api.get('/members')).data._embedded.members || [];
}

function editMember(member: Member) {
  editingMember.value = member
  form.value = {
    nick: member.nick,
    email: member.email || null,
    approved: member.approved,
  };
  showAddForm.value = false
}

function closeForm() {
  showAddForm.value = false
  editingMember.value = null
  form.value = { nick: '', email: null as string | null, approved: false };
}

async function saveMember() {
  if (editingMember.value) {
    api.put('/members/' + editingMember.value.id, form.value);
  } else {
    api.post('/members', form.value);
  }
  closeForm()
  fetchMembers()
}

onMounted(() => {
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

th,
td {
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