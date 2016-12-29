# shiftwork

This application was generated using JHipster, you can find documentation and help at [https://jhipster.github.io](https://jhipster.github.io).

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools (like
[Bower][] and [BrowserSync][]). You will only need to run this command when dependencies change in package.json.

    npm install

We use [Gulp][] as our build system. Install the Gulp command-line tool globally with:

    npm install -g gulp

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

    ./mvnw
    gulp

Bower is used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in `bower.json`. You can also run `bower update` and `bower install` to manage dependencies.
Add the `-h` flag on any command to see how you can use it. For example, `bower update -h`.


## Building for production

To optimize the shiftwork client for production, run:

    ./mvnw -Pprod clean package

This will concatenate and minify CSS and JavaScript files. It will also modify `index.html` so it references
these new files.

To ensure everything worked, run:

    java -jar target/*.war --spring.profiles.active=prod

Then navigate to [http://localhost:8080](http://localhost:8080) in your browser.

## Testing

Unit tests are run by [Karma][] and written with [Jasmine][]. They're located in `src/test/javascript/` and can be run with:

    gulp test



## Continuous Integration

To setup this project in Jenkins, use the following configuration:

* Project name: `shiftwork`
* Source Code Management
    * Git Repository: `git@github.com:xxxx/shiftwork.git`
    * Branches to build: `*/master`
    * Additional Behaviours: `Wipe out repository & force clone`
* Build Triggers
    * Poll SCM / Schedule: `H/5 * * * *`
* Build
    * Invoke Maven / Tasks: `-Pprod clean package`
* Post-build Actions
    * Publish JUnit test result report / Test Report XMLs: `build/test-results/*.xml`

[JHipster]: https://jhipster.github.io/
[Node.js]: https://nodejs.org/
[Bower]: http://bower.io/
[Gulp]: http://gulpjs.com/
[BrowserSync]: http://www.browsersync.io/
[Karma]: http://karma-runner.github.io/
[Jasmine]: http://jasmine.github.io/2.0/introduction.html
[Protractor]: https://angular.github.io/protractor/

---
## REST API

### Account Resource


Method | URL | Description  
--- | --- | ---
GET | **/api/account** | Gets account
POST | **/api/account** | Saves account 
POST | **/api/account/change_password** | Changes password
POST | **/api/account/reset_password/finish** | Finishes password reset
POST | **/api/account/reset_password/init** | Request password reset
GET | **/api/account/sessions** | Gets current sessions
DELETE | **/api/account/sessions/{series}** | Invalidates session
GET | **/api/activate** | Activates account
GET | **/api/authenticate** | Is authenticated
POST | **/api/register** | Registers account

 
Method | URL | Description  
--- | --- | ---
POST | **/api/account** | Saves account 

Parameter | Type | Content Type  
--- | --- | --- 
userDTO | application/json | body

Model
```
UserDTO {
    activated (boolean, optional),
    authorities (Array[string], optional),
    email (string, optional),
    firstName (string, optional),
    langKey (string, optional),
    lastName (string, optional),
    login (string, optional)
}
```

Model schema
```
{
   "activated": true,
   "authorities": [
     "string"
   ],
   "email": "string",
   "firstName": "string",
   "langKey": "string",
   "lastName": "string",
   "login": "string"
}
```
HTTP | Status Code 
--- | --- 
200 | OK
201	| Created
401	| Unauthorized
403	| Forbidden
404	| Not Found


### Audit Resource
Method | URL | Description
--- | --- | ---
GET | **/api/audits** | Gets all audits by dates 
GET | **/api/audits/{id}** | Gets audit by ID

### Boolean Contract Line
Method | URL | Description
--- | --- | ---
GET | **/api/boolean-contract-lines** | Gets all boolean contract lines
POST | **/api/boolean-contract-lines** | Creates boolean contract line
PUT | **/api/boolean-contract-lines** | Updates boolean contract line
DELETE | **/api/boolean-contract-lines/{id}** | Deletes boolean contract line by ID
GET | **/api/boolean-contract-lines/{id}** | Gets boolean contract lines by ID

### Contract Line Resource
Method | URL | Description
--- | --- | ---
GET | **/api/contract-lines** | Gets all contract lines
POST | **/api/contract-lines** | Creates contract line
PUT | **/api/contract-lines** | Updates contract line
DELETE | **/api/contract-lines/{id}** | Deletes contract line by ID
GET | **/api/contract-lines/{id}** | Gets contract line by ID

### Contract Resource
Method | URL | Description
--- | --- | ---
GET | **/api/contracts** | Gets all contracts
POST | **/api/contracts** | Creates contract
PUT | **/api/contracts** | Updates contract
DELETE | **/api/contracts/{id}** | Deletes contract by ID
GET | **/api/contracts/{id}** | Gets contract by ID

### Employee Absent Reason Resource
Method | URL | Description
--- | --- | ---
GET | **/api/employee-absent-reasons** | Gets all employee absent reasons
POST | **/api/employee-absent-reasons** | Creates employee absent reason
PUT | **/api/employee-absent-reasons** | Updates employee absent reason
DELETE | **/api/employee-absent-reasons/{id}** | Deletes employee absent reason by ID
GET | **/api/employee-absent-reasons/{id}** | Gets employee absent reason by ID

### Employee Day Off Request Resource
Method | URL | Description 
--- | --- | ---
GET | **/api/employee-day-off-requests** | Gets all employee day off requests
POST | **/api/employee-day-off-requests** | Creates employee day off request
PUT | **/api/employee-day-off-requests** | Updates employee day off request
DELETE | **/api/employee-day-off-requests/{id}** | Deletes employee day off request by ID
GET | **/api/employee-day-off-requests/{id}** | Gets employee day off request by ID

### Employee Day On Request Resource
Method | URL | Description
--- | --- | ---
GET | **/api/employee-day-on-requests** | Gets employee day on requests
POST | **/api/employee-day-on-requests** | Creates employee day on request
PUT | **/api/employee-day-on-requests** | Updatese employee day on request
DELETE | **/api/employee-day-on-requests/{id}** | Deletes employee day on request by ID
GET | **/api/employee-day-on-requests/{id}** | Gets employee day on request by ID

### Employee Leave Absence Resource
Method | URL | Description
--- | --- | ---
GET | **/api/employee-leave-absences** | Gets all employee leave absences
POST | **/api/employee-leave-absences** | Creates employee leave absence
PUT | **/api/employee-leave-absences** | Updates employee leave absence
DELETE | **/api/employee-leave-absences/{id}** | Deletes employee leave absence by ID
GET | **/api/employee-leave-absences/{id}** | Gets employee leave absence by ID

### Employee Resource
Method | URL | Description
--- | --- | ---
GET | **/api/employees** | Gets all employees
POST | **/api/employees** | Creates employee
PUT | **/api/employees** | Updates employee
DELETE | **/api/employees/{id}** | Deletes employee by ID
GET | **/api/employees/{id}** | Gets employee by ID

### Employee Shift Off Request Resource
Method | URL | Description
--- | --- | ---
GET | **/api/employee-shift-off-requests** | Gets all employee shift off requests
POST | **/api/employee-shift-off-requests** | Creates employee shift off request
PUT | **/api/employee-shift-off-requests** | Updates employee shift off request
DELETE | **/api/employee-shift-off-requests/{id}** | Deletes employee shift off request by ID
GET | **/api/employee-shift-off-requests/{id}** | Gets employee shift off request by ID

### Employee Shift On Request Resource
Method | URL | Description
--- | --- | ---
GET | **/api/employee-shift-on-requests** | Gets all employee shift on requests
POST | **/api/employee-shift-on-requests** | Creates employee shift on request
PUT | **/api/employee-shift-on-requests** | Updates employee shift on request
DELETE | **/api/employee-shift-on-requests/{id}** | Deletes employee shift on request by ID
GET | **/api/employee-shift-on-requests/{id}** | Gets employee shift on request by ID

### Employee Shift Resource
Method | URL | Description
--- | --- | ---
GET | **/api/employeeshifts** | Gets all employee shifts

### Employees Resource
Method | URL | Description
--- | --- | ---
GET | **/api/employees2** | Gets all employees
POST | **/api/employees2** | Creates employee
PUT | **/api/employees2** | Updates employee
DELETE | **/api/employees2/{id}** | Deletes employee by ID
GET | **/api/employees2/{id}** | Gets employee by ID

### Field Resource
Method | URL | Description
--- | --- | ---
GET | **/api/fields/employees** | Find employee by code or name

### Logs Resource
Method | URL | Description
--- | --- | ---
GET | **/api/logs** | Gets list
PUT | **/api/logs** | Changes level

### Min Max Contract Line Resource
Method | URL | Description
--- | --- | ---
GET | **/api/min-max-contract-lines** | Gets all min max contract lines
POST | **/api/min-max-contract-lines** | Creates min max contract line
PUT | **/api/min-max-contract-lines** | Updates min max contract line
DELETE | **/api/min-max-contract-lines/{id}** | Deletes min max contract line by ID
GET | **/api/min-max-contract-lines/{id}** | Gets min max contract line by ID

### Pattern Contract Line Resource
Method | URL | Description
--- | --- | ---
GET | **/api/pattern-contract-lines** | Gets all pattern contract lines
POST | **/api/pattern-contract-lines** | Creates pattern contract line
PUT | **/api/pattern-contract-lines** | Updates pattern contract line
DELETE | **/api/pattern-contract-lines/{id}** | Deletes pattern contract line by ID
GET | **/api/pattern-contract-lines/{id}** | Gets pattern contract line by ID

### Pattern Resource
Method | URL | Description
--- | --- | ---
GET | **/api/patterns** | Gets all patterns
POST | **/api/patterns** | Creates pattern
PUT | **/api/patterns** | Updates pattern
DELETE | **/api/patterns/{id}** | Deletes pattern by ID
GET | **/api/patterns/{id}** | Gets pattern by ID

### Planning Job Resource
Method | URL | Description
--- | --- | ---
GET | **/api/planning-jobs** | Gets all planning jobs
POST | **/api/planning-jobs** | Creates planning job
PUT | **/api/planning-jobs** | Updates planning job
DELETE | **/api/planning-jobs/{id}** | Deletes planning job by ID
GET | **/api/planning-jobs/{id}** | Get planning job by ID
PUT | **/api/planning-jobs/{id}** | Synchronizes planning job status

### Schedule Resource
Method | URL | Description
--- | --- | ---
GET | **/api/schedules** | Gets Shift Assignments

### Shift Assignment Resource
Method | URL | Description
--- | --- | ---
GET | **/api/shift-assignments** | Get all shift assignments
POST | **/api/shift-assignments** | Creates shift assignment
PUT | **/api/shift-assignments** | Updates shift assignment
DELETE | **/api/shift-assignments/{id}** | Deletes shift assignment by ID
GET | **/api/shift-assignments/{id}** | Get shift assignment by ID

### Shift Date Resource
Method | URL | Description
--- | --- | ---
GET | **/api/shift-dates** | Gets all shift dates
POST | **/api/shift-dates** | Creates shift date
PUT | **/api/shift-dates** | Update shift date
DELETE | **/api/shift-dates/{id}** | Deletes shift date by ID
GET | **/api/shift-dates/{id}** | Gets shift date by ID

### Shift Resource
Method | URL | Description
--- | --- | ---
GET | **/api/shifts** | Gets all shifts
POST | **/api/shifts** | Creates shift
PUT | **/api/shifts** | Updates shift
DELETE | **/api/shifts/{id}** | Delete shift by ID
GET | **/api/shifts/{id}** | Get shift by ID

### Shift Type Resource
Method | URL | Description
--- | --- | ---
GET | **/api/shift-types** | Gets all shift types
POST | **/api/shift-types** | Creates shift type
PUT | **/api/shift-types** | Updates shift type
DELETE | **/api/shift-types/{id}** | Deletes shift type by ID
GET | **/api/shift-types/{id}** | Gets shift type by ID

###Skill Proficiency Resource
Method | URL | Description
--- | --- | ---
GET | **/api/skill-proficiencies** | Set all skill proficiencies
POST | **/api/skill-proficiencies** | Create skill proficiency
PUT | **/api/skill-proficiencies** | Updates skill proficiency
DELETE | **/api/skill-proficiencies/{id}** | Deletes skill proficiency by ID
GET | **/api/skill-proficiencies/{id}** | Gets skill proficiency by ID

### Skill Resource
Method | URL | Description
--- | --- | ---
GET | **/api/skills** | Gets all skills
POST | **/api/skills** | Creates Skill
PUT | **/api/skills** | Updates skill
DELETE | **/api/skills/{id}** | Deletes skill by ID
GET | **/api/skills/{id}** | Get skill by ID


### Staff Roster Parametrization Resource
Method | URL | Description
--- | --- | ---
GET | **/api/staff-roster-parametrizations** | Gets all staff roster parametrizations
POST | **/api/staff-roster-parametrizations** | Creates staff roster parametrization
PUT | **/api/staff-roster-parametrizations** | Updates staff roster parametrization
DELETE | **/api/staff-roster-parametrizations/{id}** | Deletes staff roster parametrization by ID
GET | **/api/staff-roster-parametrizations/{id}** | Gets staff roster parametrization by ID

### Staff Roster Resource
Method | URL | Description
--- | --- | ---
POST | **/api/staff-rosters** | Creates staff roster

### Task Resource
Method | URL | Description
--- | --- | ---
GET | **/api/tasks** | Get all tasks
POST | **/api/tasks** | Create task
PUT | **/api/tasks** | UpdateTask
DELETE | **/api/tasks/{id}** | Deletes task by ID
GET | **/api/tasks/{id}** | Gets task by ID

### Task Skill Requirement Resource
Method | URL | Description
--- | --- | ---
GET | **/api/task-skill-requirements** | Gets all task skill requirements
POST | **/api/task-skill-requirements** | Creates task skill requirement
PUT | **/api/task-skill-requirements** | Updates task skill requirement
DELETE | **/api/task-skill-requirements/{id}** | Deletes task skill requirement by ID
GET | **/api/task-skill-requirements/{id}** | Gets task skill requirement by ID

### Task Type Resource
Method | URL | Description
--- | --- | ---
GET | **/api/task-tpyes** | Gets all task types
POST | **/api/task-tpyes** | Creates task type
PUT | **/api/task-tpyes** | Updates task type
DELETE | **/api/task-tpyes/{id}** | Deletes task type
GET | **/api/task-tpyes/{id}** | Gets task type

### User Resource
Method | URL | Description
--- | --- | ---
GET | **/api/users** | Gets all users
POST | **/api/users** | Creates user
PUT | **/api/users** | Updates user
DELETE | **/api/users/{login}** | Deletes user by login
GET | **/api/users/{login}** | Gets user by login


----------
### Weekend Definition Resource
Response Class (Status 200)

Method | URL | Description
--- | --- | ---
GET | **/api/weekend-definitions** | Gets all weekend definitions

Model
```
Inline Model [
	WeekendDefinition
]
WeekendDefinition {
	days (Array[string], optional),
	description (string, optional),
	id (integer, optional)
}
```
Model schema
```
[
  {
    "days": [
      "MONDAY"
    ],
    "description": "string",
    "id": 0
  }
]
```
Response Content Type: application/json

HTTP | Status Code
---|---
401	| Unauthorized
403	| Forbidden
404	| Not Found

Method | URL | Description
--- | --- | ---
POST | **/api/weekend-definitions** | Creates weekend definition
Response Class (Status 200) OK

Model
```
WeekendDefinition {
days (Array[string], optional),
description (string, optional),
id (integer, optional)
}
```
Model schema
```
{
  "days": [
    "MONDAY"
  ],
  "description": "string",
  "id": 0
}
```
Parameter content type: application/json

Method | URL | Description
--- | --- | ---
GET | **/api/weekend-definitions** | Gets all weekend definitions
POST | **/api/weekend-definitions** | Creates weekend
PUT | **/api/weekend-definitions** | Update weekend definition
DELETE | **/api/weekend-definitions/{id}** | Deletes weekend definition
GET | **/api/weekend-definitions/{id}** | Gets weekend definition
