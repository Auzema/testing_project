# AI-Supported Test Data Generation for Software Testing

## 1. Project Information

| Item | Details |
|---|---|
| Course project topic | AI-Supported Test Data Generation for Software Testing |
| System Under Test | Conduit RealWorld Web Application |
| Backend | Node.js, Express, Prisma, SQLite |
| Frontend | React, Redux |
| Automation tool | Katalon Studio |
| Testing approach | Manual testing, data-driven automation, AI-assisted test data generation |
| Repository | https://github.com/Auzema/testing_project |

## 2. Project Objective

The objective of this project is to apply software testing activities to a real-world open-source web application and use AI to support test data generation. The AI-generated data will be reviewed, cleaned, and used for manual test cases and Katalon data-driven automated tests.

The project focuses on:

- Requirement analysis for testing
- Test strategy and planning
- Test case design
- Test data generation using AI
- Manual test execution
- Defect reporting
- Katalon data-driven automation
- AI usage documentation

## 3. System Overview

Conduit is a RealWorld social blogging web application. It allows users to register, log in, create articles, browse articles, add comments, favorite articles, follow profiles, and update user settings.

### Why Conduit was chosen as the System Under Test

Conduit (RealWorld) is a widely used open-source full-stack reference application. It satisfies the course requirement of selecting an open-source system, and is well suited for an eight-week testing project for the following reasons:

- It exposes a complete REST API (`/api/users`, `/api/articles`, `/api/profiles`, `/api/tags`) that is small enough to be analyzed in detail but large enough to support diverse manual and automated test cases.
- It contains realistic input forms (registration, login, article editor, comments, settings) that benefit from AI-generated valid, invalid, and boundary test data.
- It can be deployed locally with Node.js + SQLite without paid services or complex infrastructure.
- It has clear server-side validation rules that make pass/fail expectations easy to define for data-driven Katalon tests.

### Main Features

| Feature | Description |
|---|---|
| User registration | Create a new user account with username, email, and password |
| Login and logout | Authenticate users and manage sessions |
| Article management | Create, view, update, and delete articles |
| Comment management | Add and delete comments on articles |
| Tags | Display and use article tags |
| Profile | View author profiles and favorite articles |
| Settings | Update user profile information |

## 4. Deployment Plan

### Backend

```bash
cd node-express-realworld-example-app
npm install
PORT=3001 npm start
```

Backend API URL:

```text
http://localhost:3001/api
```

### Frontend

```bash
cd react-redux-realworld-example-app
npm install
BROWSER=none npm start
```

Frontend URL:

```text
http://localhost:4100/
```

If the frontend fails on a newer Node.js version because of an OpenSSL/Webpack issue:

```bash
NODE_OPTIONS=--openssl-legacy-provider BROWSER=none npm start
```

## 5. Test Strategy and Plan

### Test Objectives

- Verify that core Conduit functions work correctly.
- Validate input handling using valid, invalid, boundary, and edge-case test data.
- Use AI to generate broad and diverse test data.
- Use Katalon Studio to automate selected data-driven test scenarios.
- Record test results and defects using the required course templates.

### Test Scope

| In Scope | Out of Scope |
|---|---|
| Register | Performance testing |
| Login and logout | Security penetration testing |
| Create article | Browser compatibility beyond selected browsers |
| Create comment | Mobile app testing |
| Update settings/profile | Backend unit test development |
| Tags and article list display | Production deployment testing |

### Testing Techniques

| Technique | Usage |
|---|---|
| Equivalence Partitioning | Separate valid and invalid input classes |
| Boundary Value Analysis | Test minimum, maximum, empty, and long input values |
| Negative Testing | Verify system behavior with invalid data |
| Decision Table Testing | Cover combinations of login/register input conditions |
| State Transition Testing | Verify behavior before login, after login, and after logout |
| Data-Driven Testing | Run the same Katalon test case with multiple input rows |

### Test Environment

| Component | Environment |
|---|---|
| Operating system | Windows for development; macOS-compatible run guide |
| Browser | Chrome |
| Backend runtime | Node.js |
| Database | SQLite through Prisma |
| Frontend port | 4100 |
| Backend port | 3001 |
| Automation tool | Katalon Studio |

## 6. AI-Supported Test Data Generation Plan

AI will be used to generate candidate test data for forms and workflows. The generated data will not be accepted blindly. The team will review, correct, deduplicate, and format the data before using it in manual test cases or Katalon data files.

### Test Data Scope

| Module | AI-Generated Test Data |
|---|---|
| Register | Valid usernames, invalid emails, duplicate usernames, weak passwords, empty fields |
| Login | Valid credentials, wrong password, unknown email, invalid email format, blank fields |
| Article | Normal title, empty title, long title, special characters, long body, tag combinations |
| Comment | Normal comment, empty comment, long comment, special characters |
| Settings/Profile | Valid bio, long bio, image URL, invalid image URL, updated email |
| Tags | Common tags, duplicate tags, special-character tags |

### Data Quality Rules

| Rule | Purpose |
|---|---|
| Remove duplicate rows | Avoid repeated test execution with the same data |
| Separate valid and invalid data | Make expected results clear |
| Keep expected result column | Support Katalon assertions |
| Use realistic values | Simulate real user behavior |
| Include boundary values | Improve defect detection |
| Avoid sensitive personal data | Keep the test data safe and shareable |

### Planned Katalon Data Files

| Data File | Example Columns |
|---|---|
| `TD_Register.xlsx` | username, email, password, expectedResult |
| `TD_Login.xlsx` | email, password, expectedResult |
| `TD_Article.xlsx` | title, description, body, tags, expectedResult |
| `TD_Comment.xlsx` | commentBody, expectedResult |
| `TD_Profile.xlsx` | username, bio, imageUrl, email, expectedResult |

## 7. Test Case Design Plan

The final report will use the required course test case template:

| ID | Test Scenario | Steps | Expected Result | Actual Result | Status |
|---|---|---|---|---|---|
| TC_REG_01 | Register with valid AI-generated user data | 1. Open Register page<br>2. Enter valid username, email, and password<br>3. Click Sign up | User account is created and user is logged in | To be updated during execution | To be updated |
| TC_REG_02 | Register with invalid email format | 1. Open Register page<br>2. Enter valid username<br>3. Enter invalid email<br>4. Enter valid password<br>5. Click Sign up | System shows validation or registration error | To be updated during execution | To be updated |
| TC_LOGIN_01 | Login with valid credentials | 1. Open Login page<br>2. Enter valid email and password<br>3. Click Sign in | User logs in successfully | To be updated during execution | To be updated |
| TC_LOGIN_02 | Login with wrong password | 1. Open Login page<br>2. Enter valid email<br>3. Enter wrong password<br>4. Click Sign in | System rejects login and shows error | To be updated during execution | To be updated |
| TC_ART_01 | Create article with valid generated data | 1. Login<br>2. Open New Article page<br>3. Enter title, description, body, and tags<br>4. Publish article | Article is created and displayed | To be updated during execution | To be updated |
| TC_ART_02 | Create article with empty title | 1. Login<br>2. Open New Article page<br>3. Leave title empty<br>4. Enter other fields<br>5. Publish article | System rejects article creation or shows validation error | To be updated during execution | To be updated |
| TC_COM_01 | Add valid comment to article | 1. Login<br>2. Open article detail page<br>3. Enter valid comment<br>4. Submit comment | Comment is added successfully | To be updated during execution | To be updated |
| TC_SET_01 | Update profile with generated valid data | 1. Login<br>2. Open Settings page<br>3. Update bio/image/email<br>4. Save settings | Profile information is updated | To be updated during execution | To be updated |

### Execution Summary (to be filled after Week 6)

| Metric | Value |
|---|---|
| Total test cases designed | _ |
| Total test cases executed | _ |
| Passed | _ |
| Failed | _ |
| Blocked / Not executed | _ |
| Pass rate (%) | _ |
| Defects logged | _ |

## 8. Defect Report Plan

The final report will use the required course defect template:

| Bug ID | Description | Steps | Severity | Status |
|---|---|---|---|---|
| BUG_01 | To be updated after execution | To be updated after execution | To be classified | Open |
| BUG_02 | To be updated after execution | To be updated after execution | To be classified | Open |

Severity levels:

| Severity | Meaning |
|---|---|
| Critical | System crash or core feature cannot be used |
| High | Major user workflow is blocked |
| Medium | Feature works incorrectly but workaround exists |
| Low | Minor UI or message issue |

## 9. Katalon Automation Plan

### Automation Scope

| Katalon Test Case | Data Source | Purpose |
|---|---|---|
| `TC_Katalon_Register_DataDriven` | `TD_Register.xlsx` | Verify registration with multiple generated data rows |
| `TC_Katalon_Login_DataDriven` | `TD_Login.xlsx` | Verify login with valid and invalid credentials |
| `TC_Katalon_CreateArticle_DataDriven` | `TD_Article.xlsx` | Verify article creation with different data types |
| `TC_Katalon_AddComment_DataDriven` | `TD_Comment.xlsx` | Verify comment creation with generated comment data |
| `TC_Katalon_UpdateProfile_DataDriven` | `TD_Profile.xlsx` | Verify profile update with generated data |

### Katalon Execution Instructions

1. Start backend at `http://localhost:3001/api`.
2. Start frontend at `http://localhost:4100/`.
3. Open Katalon Studio.
4. Open the Katalon project.
5. Add Excel or CSV files under Katalon `Data Files`.
6. Bind data columns to test case variables.
7. Run the test suite.
8. Export logs, screenshots, and execution result summary.

### Expected Automation Evidence

- Katalon test suite result summary
- Pass/fail execution logs
- Screenshots for failed cases
- Data files used for execution
- Short explanation of failed automated cases

## 10. AI Usage Report Plan

### AI Tool Used

ChatGPT or another AI assistant will be used to generate candidate test data, suggest edge cases, and format data tables for Katalon.

### Purpose

| Purpose | Description |
|---|---|
| Generate test data | Create valid, invalid, boundary, and edge-case data |
| Improve coverage | Suggest input values that testers may miss |
| Format test data | Convert generated values into table format for Katalon |
| Support documentation | Help draft test data explanation and AI usage report |

### Example Prompts

```text
Generate valid and invalid test data for a user registration form with username, email, and password fields.
```

```text
Generate boundary value and negative test data for login testing in a web application.
```

```text
Create article test data for a blogging system, including normal, empty, long, duplicate, and special-character input values.
```

```text
Generate comment test data for valid comments, empty comments, long comments, and comments with special characters.
```

```text
Convert the generated test data into a table format suitable for importing into Katalon Studio as an Excel data file.
```

### AI Verification Method

| Step | Verification Activity |
|---|---|
| 1 | Review AI output manually |
| 2 | Remove unrealistic or duplicated values |
| 3 | Check that each data row has a clear expected result |
| 4 | Confirm that data matches the Conduit input fields |
| 5 | Execute selected rows manually before automation |
| 6 | Use Katalon execution results to validate whether the generated data is useful |

## 11. Eight-Week Project Schedule

| Week | Activity | Deliverable |
|---|---|---|
| 1 | Team formation and role assignment | Team role table |
| 2 | Select and deploy Conduit application | System overview and deployment guide |
| 3 | Analyze features and define test plan | Test scope, objectives, strategy |
| 4 | Generate AI-supported test data | Draft test data tables |
| 5 | Design manual test cases using generated data | Test case document |
| 6 | Execute manual tests and record results | Execution log and defect report |
| 7 | Create Katalon data-driven automation | Katalon scripts, data files, automation results |
| 8 | Complete final report and presentation | Final testing report and slides |

## 12. Team Role Plan

The team consists of three members. Responsibilities are distributed as follows:

| Member | Role | Responsibility |
|---|---|---|
| [Student Name 1] | Test Lead & Test Analyst | Manage test plan, schedule, and scope; coordinate the final report; analyze Conduit requirements and identify testable features |
| [Student Name 2] | Test Designer | Design manual test cases, review and clean AI-generated test data, prepare Katalon data files |
| [Student Name 3] | Automation Tester | Implement Katalon data-driven test cases, run the test suite, and collect execution evidence |

Note: All members participate in review, manual test execution, AI prompt verification, and presentation preparation.

## 13. Final Report Mapping

| Required Report Section | Planned Content |
|---|---|
| 1. System Overview | Conduit description, main functions, deployment guide |
| 2. Test Strategy and Plan | Objectives, scope, strategy, environment, eight-week schedule |
| 3. Test Cases | Manual test cases using the required template |
| 4. Execution Summary and Defects | Pass/fail summary and defect table |
| 5. Automation Results | Katalon data-driven testing scope, instructions, results |
| 6. AI Usage Report | AI tool, purpose, prompts, generated data review process |
| 7. Lessons Learned | Deployment issues, test data quality, Katalon automation experience, AI limitations |

## 14. Presentation Outline

The final presentation follows the course requirement of 10–12 minutes plus 5 minutes Q&A. Time budget and slide mapping:

| # | Slide / Topic | Duration | Owner | Content |
|---|---|---:|---|---|
| 1 | Title and team introduction | 0:30 | Test Lead | Project topic, SUT, team members and roles |
| 2 | System Under Test | 1:30 | Test Lead | Conduit overview, main features, deployment summary |
| 3 | Testing strategy | 2:00 | Test Lead | Scope, objectives, techniques, environment, schedule |
| 4 | Test cases and test data | 2:00 | Test Designer | Sample test cases, AI-generated data tables, data quality rules |
| 5 | Defects discovered | 1:30 | Test Designer | Severity breakdown and 2–3 representative defects |
| 6 | Automation demo | 2:30 | Automation Tester | Live Katalon data-driven run, pass/fail summary, screenshots |
| 7 | AI usage report | 1:30 | Test Lead | AI tool, real prompts used, verification process, lessons |
| 8 | Lessons learned and Q&A handover | 0:30 | All | Key takeaways and transition to Q&A |

Total: ~12 minutes presentation + 5 minutes Q&A.

## 15. Lessons Learned (to be filled after Week 8)

This section will be completed at the end of the project. Planned topics to reflect on:

- Deployment of the Conduit frontend and backend on Windows (Node version, OpenSSL legacy provider, port configuration).
- Quality of AI-generated test data: usefulness, duplication, unrealistic values, and the impact of human review.
- Effectiveness of Katalon data-driven testing: ease of binding data files, handling of dynamic elements, and stability of test runs.
- Comparison of defects found through manual exploratory testing versus data-driven automation.
- Limitations of AI in testing (lack of real domain context, hallucinated values, need for human verification) and how the team mitigated them.
- Teamwork lessons: coordination across the three roles, code/data review process, and time management over the 8-week schedule.

## 16. Deliverables Checklist

- [ ] Final testing report in English
- [ ] Test plan
- [ ] Test case table
- [ ] Execution summary table
- [ ] Defect report table
- [ ] AI-generated test data files
- [ ] AI prompt and verification log
- [ ] Katalon automation project or scripts
- [ ] Katalon execution logs and screenshots
- [ ] Presentation slides

