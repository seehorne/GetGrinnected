# (1) External Documentation and Stakeholder Meetings

> Description (TODO: DELETEME before completion)
>
> In your Sprint Journal, add an entry that includes the following for each stakeholder:
>
> - Who you met with
>   - Name, occupation, and other relevant information about the stakeholder
>   - Date and time of the stakeholder meeting
> - Description of the test session and feedback
>   - What was their behavior of interacting with the documentation?
>   - Did they succeed or get stuck?
>   - What feedback did they provide about documentation?
>   - What questions did you ask, and what were the stakeholder's answers?

# (2) Internal Documentation Lab

## Group Anthony and Michael

### A: Anthony B: Michael

We worked on the login process code for Kotlin.

We made no code changes but furthered documentation. We added inline comments above the async functions in the AuthModel file. We added documentation to the Login Screen file specifically to explain which chunks corresponded to which UI elements and what all they did (specifically useful for the login button). Added inline comments to specific branching/conditionals that may be confusing when not knowing what is being evaluated.

The function headers and data class headers were helpful in understanding what each data class was used for or what a function did specifically. 

Hash of commit [2d3da7a](https://github.com/seehorne/GetGrinnected/commit/2d3da7a58e94008b983e033657d2b8fefc0dc63b)

Our issue tracker was up to date on what the next step entailed.

### A: Michael B: Anthony

We worked on the homepage for Swift.

We made no code changes but furthered documentation. We added a header comment to the enum class for tags. We added a header comment to the home screen discussing what each UI option did so that it was easy to discern what they were used for.

The in line comments for the homescreen were helpful specifically for which brackets closed which statements. Additionally the comments on the specific values of the variables.

Hash of commit [b679543](https://github.com/seehorne/GetGrinnected/commit/b679543f27ed9e8f232e589c698d09ed25adbddc)

## Group Budhil and almond

### A: almond B: Budhil

We looked at `src/backend/api.js`, lines 118 through 225 (functions `getEventsBetween`, `parseParamDate`, and `parseQueryTags`).

We agreed that, although there were already comments on many relevant parts of the code, these comments tended not to be helpful from an external perspective. Because of that, we decided they should be modified to better explain themselves to a reader who is less familiar with the code. To summarize, a lot of documentation needed to answer the question "where does this variable come from?" or "What does that function call do in the scope of the whole thing?"

Existing documentation did a decent job of saying what each thing did in most cases, but lacked enough depth to be easily understood. 

For this one, we made changes across more than one commit so I can't give you a hash. However, I can give you a link to the PR page that shows
exactly what was changed based on this lab: <https://github.com/seehorne/GetGrinnected/pull/64/files>

> If you *really* want a commit hash, most changes are part of commit [ac70e9d](https://github.com/seehorne/GetGrinnected/commit/ac70e9dd6d85291ea54f5c202a1b3a6b3eb28f89).

### A: Budhil B: almond

We looked at the way the event cards were defined for Swift. We didn't look at specific lines, more jumping around to different parts of the code as we needed to in order to understand what was going on.

In this case, a lot of the documentation changes we made were to add new information where there had previously been no comments. Budhil brought up that this was something Michael had wanted when reviewing code as well. In particular, we wrote documentation centering around the questions of "Why does this struct have to have these items in it?" and "There's a lot going on here, how can I understand it overall?"

There wasn't much previous documentation that could help us in this case.

You can see our changes at the commit with hash [695e0af](https://github.com/seehorne/GetGrinnected/pull/62/commits/695e0affb3f11506795e6bde4454b452f7f8c2c7).

## Group Ellie and Ethan

> TODO: DELETEME ON COMPLETION
> THIS SECTION STILL NEEDS TO BE FILLED OUT.

# (3) Self-Selected Work toward Minimum Viable Product (MVP)

> Description (TODO: DELETEME ON COMPLETION)
> - In a Sprint Journal entry, remind us what your MVP is (look back to Milestone 1 where you described what features would be included in your MVP)
> - Describe what work, if any, remains toward delivering your MVP

# (4) **Generative AI** Experiment

> Description (TODO: DELETEME WHEN STUFF IS ADDED)
>
> For each use of AI this sprint, write an entry in your Sprint Journal including:
> 
> 1. Name the members of your team who tried it.
> 2. Going into this use of AI, what were your goals and expectations?
> 3. Describe the use specifically in detail. How did you prompt the AI, and what was its output?
> 4. How did this use of AI affect your product development or other sprint deliverables? If you integrated any of its output directly into your code base, include a link to a pull request where the generated output can be clearly distinguished.
> 5. Refer back to your answer to question 2. To what extent did the use of AI achieve your goals and conform to your expectations?