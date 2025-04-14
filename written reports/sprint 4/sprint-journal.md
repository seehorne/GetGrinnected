# Internal Documentation Lab

## Group Anthony and Michael

### A: Anthony B: Michael

We worked on the login process code for Kotlin.

We made no code changes but furthered documentation. We added inline comments above the async functions in the AuthModel file. We added documentation to the Login Screen file specifically to explain which chunks corresponded to which UI elements and what all they did (specifically useful for the login button). Added inline comments to specific branching/conditionals that may be confusing when not knowing what is being evaluated.

The function headers and data class headers were helpful in understanding what each data class was used for or what a function did specifically. 

Hash of commit [2d3da7a58e94008b983e033657d2b8fefc0dc63b](https://github.com/seehorne/GetGrinnected/commit/2d3da7a58e94008b983e033657d2b8fefc0dc63b)

Our issue tracker was up to date on what the next step entailed.

### A: Michael B: Anthony

We worked on the homepage for Swift.

We made no code changes but furthered documentation. We added a header comment to the enum class for tags. We added a header comment to the home screen discussing what each UI option did so that it was easy to discern what they were used for.

The in line comments for the homescreen were helpful specifically for which brackets closed which statements. Additionally the comments on the specific values of the variables.

Hash of commit [b679543f27ed9e8f232e589c698d09ed25adbddc](https://github.com/seehorne/GetGrinnected/commit/b679543f27ed9e8f232e589c698d09ed25adbddc)

## Group Budhil and almond

### A: almond B: Budhil

We looked at `src/backend/api.js`, lines 118 through 225 (functions `getEventsBetween`, `parseParamDate`, and `parseQueryTags`).

We agreed that, although there were already comments on many relevant parts of the code, these comments tended not to be helpful from an external perspective. Because of that, we decided they should be modified to better explain themselves to a reader who is less familiar with the code. To summarize, a lot of documentation needed to answer the question "where does this variable come from?" or "What does that function call do in the scope of the whole thing?"

Existing documentation did a decent job of saying what each thing did in most cases, but lacked enough depth to be easily understood. 

You can see these changes at the commit with hash [ac70e9d](https://github.com/seehorne/GetGrinnected/commit/ac70e9dd6d85291ea54f5c202a1b3a6b3eb28f89).

### A: Budhil B: almond

We looked at the way the event cards were defined for Swift. We didn't look at specific lines, more jumping around to different parts of the code as we needed to in order to understand what was going on.

In this case, a lot of the documentation changes we made were to add new information where there had previously been no comments. Budhil brought up that this was something Michael had wanted when reviewing code as well. In particular, we wrote documentation centering around the questions of "Why does this struct have to have these items in it?" and "There's a lot going on here, how can I understand it overall?"

There wasn't much previous documentation that could help us in this case.

You can see our changes at the commit with hash [695e0af](https://github.com/seehorne/GetGrinnected/pull/62/commits/695e0affb3f11506795e6bde4454b452f7f8c2c7).