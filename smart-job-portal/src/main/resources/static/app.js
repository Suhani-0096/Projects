let currentUser = null;
let allJobs = [];

const $ = id => document.getElementById(id);
function toast(msg){ $('toast').textContent=msg; $('toast').style.display='block'; setTimeout(()=>$('toast').style.display='none',2500); }
async function api(url, options={}) {
  const res = await fetch(url, {headers:{'Content-Type':'application/json', ...(options.headers||{})}, ...options});
  const data = await res.json().catch(()=>({}));
  if(!res.ok) throw new Error(data.error || 'Request failed');
  return data;
}

async function loadJobs(){
  allJobs = await api('/api/jobs');
  renderJobs(allJobs);
}
function renderJobs(jobs){
  $('jobs').innerHTML = jobs.length ? jobs.map(j => `
    <article class="job">
      <h3>${esc(j.title)}</h3>
      <div class="meta"><b>${esc(j.company)}</b> · ${esc(j.location)}</div>
      <div class="meta">Salary: ₹${Number(j.salary||0).toLocaleString('en-IN')}</div>
      <p>${esc(j.description)}</p>
      <div class="skills">${esc(j.skills)}</div>
      ${currentUser?.role === 'JOB_SEEKER' ? `<br><button onclick="applyJob(${j.id})">Apply Now</button>` : ''}
    </article>`).join('') : '<p>No jobs available.</p>';
}
function esc(s){return String(s??'').replace(/[&<>"']/g,m=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#039;'}[m]));}

$('loginForm').addEventListener('submit', async e=>{
  e.preventDefault();
  try{const d=await api('/api/auth/login',{method:'POST',body:JSON.stringify({email:$('loginEmail').value,password:$('loginPassword').value})}); currentUser=d.user; setup(); toast('Login successful');}
  catch(e){toast(e.message)}
});
$('registerForm').addEventListener('submit', async e=>{
  e.preventDefault();
  try{await api('/api/auth/register',{method:'POST',body:JSON.stringify({name:$('regName').value,email:$('regEmail').value,password:$('regPassword').value,role:$('regRole').value})}); toast('Registered. Please login.'); e.target.reset();}
  catch(e){toast(e.message)}
});
$('search').addEventListener('input', e=>{
  const q=e.target.value.toLowerCase();
  renderJobs(allJobs.filter(j=>[j.title,j.company,j.location,j.skills].join(' ').toLowerCase().includes(q)));
});
$('jobForm').addEventListener('submit', async e=>{
  e.preventDefault();
  try{
    await api('/api/jobs',{method:'POST',body:JSON.stringify({
      title:$('jobTitle').value,company:$('jobCompany').value,location:$('jobLocation').value,
      skills:$('jobSkills').value,salary:Number($('jobSalary').value||0),description:$('jobDescription').value
    })});
    e.target.reset(); toast('Job published'); await loadDashboard(); await loadJobs();
  }catch(e){toast(e.message)}
});

async function applyJob(id){
  try{await api(`/api/jobs/${id}/apply`,{method:'POST'});toast('Application submitted');}
  catch(e){toast(e.message)}
}
async function loadDashboard(){
  if(!currentUser)return;
  $('dashboard').classList.remove('hidden');
  $('dashboardTitle').textContent=`Welcome, ${currentUser.name}`;
  if(currentUser.role==='RECRUITER'){
    $('recruiterPanel').classList.remove('hidden'); $('seekerPanel').classList.add('hidden');
    const jobs=await api('/api/recruiter/jobs');
    $('myJobs').innerHTML=jobs.length?jobs.map(j=>`<div class="job"><b>${esc(j.title)}</b> - ${esc(j.company)} <button class="small danger" onclick="deleteJob(${j.id})">Delete</button><p>${esc(j.location)} · ${esc(j.skills)}</p></div>`).join(''):'<p>No jobs posted yet.</p>';
    const apps=await api('/api/recruiter/applications');
    $('recruiterApplications').innerHTML=apps.length?`<table class="table"><tr><th>Job</th><th>Applicant</th><th>Email</th><th>Status</th><th>Action</th></tr>${apps.map(a=>`<tr><td>${esc(a.jobTitle)}</td><td>${esc(a.applicantName)}</td><td>${esc(a.applicantEmail)}</td><td>${esc(a.status)}</td><td><select onchange="updateStatus(${a.id},this.value)"><option>APPLIED</option><option>SHORTLISTED</option><option>REJECTED</option></select></td></tr>`).join('')}</table>`:'<p>No applications yet.</p>';
  }else{
    $('seekerPanel').classList.remove('hidden'); $('recruiterPanel').classList.add('hidden');
    const apps=await api('/api/applications/me');
    $('myApplications').innerHTML=apps.length?`<table class="table"><tr><th>Job</th><th>Company</th><th>Status</th><th>Applied</th></tr>${apps.map(a=>`<tr><td>${esc(a.jobTitle)}</td><td>${esc(a.company)}</td><td>${esc(a.status)}</td><td>${new Date(a.appliedAt).toLocaleString()}</td></tr>`).join('')}</table>`:'<p>You have not applied to any jobs.</p>';
  }
}
async function deleteJob(id){if(!confirm('Delete this job?'))return;try{await api(`/api/jobs/${id}`,{method:'DELETE'});toast('Job deleted');await loadDashboard();await loadJobs()}catch(e){toast(e.message)}}
async function updateStatus(id,status){try{await api(`/api/applications/${id}/status`,{method:'PUT',body:JSON.stringify({status})});toast('Status updated')}catch(e){toast(e.message)}}

function setup(){
  $('auth').classList.add('hidden'); $('hero').classList.add('hidden');
  $('userArea').innerHTML=`<div class="user"><span>${esc(currentUser.name)} (${currentUser.role.replace('_',' ')})</span><button onclick="logout()">Logout</button></div>`;
  renderJobs(allJobs); loadDashboard();
}
async function logout(){await api('/api/auth/logout',{method:'POST'});location.reload();}
(async()=>{try{const d=await api('/api/auth/me');currentUser=d.user}catch(e){} await loadJobs(); if(currentUser)setup();})();
