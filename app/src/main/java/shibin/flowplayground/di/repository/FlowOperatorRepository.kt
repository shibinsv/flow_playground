package shibin.flowplayground.di.repository

import shibin.flowplayground.data.FlowOperator
import shibin.flowplayground.enums.DemoType
import javax.inject.Inject

class FlowOperatorRepository @Inject constructor() {

    fun getOperators(): List<FlowOperator> = listOf(

        // 🔹 MAP
        FlowOperator(
            id = "map",
            name = "map",
            description = "Transforms each emitted value using a given function.",
            impact = "Every emission is transformed. 1-to-1 relationship between input and output.",
            codeSnippet = "...",
            demoType = DemoType.MAP,
            explanationSteps = listOf(
                "Flow emits values: 1, 2, 3",
                "Each value enters the map operator",
                "Transformation is applied to each value",
                "1 becomes 2, 2 becomes 4, 3 becomes 6",
                "Final output is 2, 4, 6"
            ),
            exampleInput = listOf("1", "2", "3"),
            exampleOutput = listOf("2", "4", "6")
        ),

        // 🔹 FILTER
        FlowOperator(
            id = "filter",
            name = "filter",
            description = "Emits only values that satisfy the given predicate.",
            impact = "Reduces emissions by dropping values.",
            codeSnippet = "...",
            demoType = DemoType.FILTER,
            explanationSteps = listOf(
                "Flow emits values: 1, 2, 3, 4, 5",
                "Each value is checked against a condition",
                "Only even numbers pass the filter",
                "Odd numbers are removed",
                "Final output is 2 and 4"
            ),
            exampleInput = listOf("1", "2", "3", "4", "5"),
            exampleOutput = listOf("2", "4")
        ),

        // 🔹 FLATMAP LATEST
        FlowOperator(
            id = "flatMapLatest",
            name = "flatMapLatest",
            description = "Cancels previous flow when new value arrives.",
            impact = "Only latest flow is active.",
            codeSnippet = "...",
            demoType = DemoType.FLAT_MAP_LATEST,
            explanationSteps = listOf(
                "Flow emits values A, B, C",
                "A starts a new inner flow",
                "Before A finishes, B arrives and cancels A",
                "B starts a new flow but gets cancelled by C",
                "Only C completes and emits results"
            ),
            exampleInput = listOf("A", "B", "C"),
            exampleOutput = listOf("Result(C)")
        ),

        // 🔹 FLATMAP MERGE
        FlowOperator(
            id = "flatMapMerge",
            name = "flatMapMerge",
            description = "Runs multiple flows in parallel.",
            impact = "All flows run concurrently.",
            codeSnippet = "...",
            demoType = DemoType.FLAT_MAP_MERGE,
            explanationSteps = listOf(
                "Flow emits values: 1, 2, 3",
                "Each value starts its own inner flow",
                "All inner flows run at the same time",
                "Results arrive whenever ready",
                "Order of output may vary"
            ),
            exampleInput = listOf("1", "2", "3"),
            exampleOutput = listOf("1a", "2a", "1b", "3a")
        ),

        // 🔹 DEBOUNCE
        FlowOperator(
            id = "debounce",
            name = "debounce",
            description = "Emits only after delay with no new values.",
            impact = "Suppresses rapid emissions.",
            codeSnippet = "...",
            demoType = DemoType.DEBOUNCE,
            explanationSteps = listOf(
                "User types: h, he, hel, hello",
                "Each new input resets the timer",
                "Debounce waits for a pause",
                "Only the final stable value is emitted",
                "Final output is hello"
            ),
            exampleInput = listOf("h", "he", "hel", "hello"),
            exampleOutput = listOf("hello")
        ),

        // 🔹 DISTINCT
        FlowOperator(
            id = "distinctUntilChanged",
            name = "distinctUntilChanged",
            description = "Removes consecutive duplicates.",
            impact = "Avoids redundant emissions.",
            codeSnippet = "...",
            demoType = DemoType.DISTINCT_UNTIL_CHANGED,
            explanationSteps = listOf(
                "Flow emits: 1, 1, 2, 2, 3",
                "Operator compares with previous value",
                "Duplicate consecutive values are ignored",
                "Only changes are emitted",
                "Final output is 1, 2, 3"
            ),
            exampleInput = listOf("1", "1", "2", "2", "3"),
            exampleOutput = listOf("1", "2", "3")
        ),

        // 🔹 ZIP
        FlowOperator(
            id = "zip",
            name = "zip",
            description = "Pairs values from two flows.",
            impact = "Waits for both flows.",
            codeSnippet = "...",
            demoType = DemoType.ZIP,
            explanationSteps = listOf(
                "Two flows emit values",
                "First values are paired together",
                "Second values are paired next",
                "Stops when one flow ends",
                "Output is paired values"
            ),
            exampleInput = listOf("A+1", "B+2", "C+3"),
            exampleOutput = listOf("A1", "B2", "C3")
        ),

        // 🔹 COMBINE
        FlowOperator(
            id = "combine",
            name = "combine",
            description = "Combines latest values from flows.",
            impact = "Reacts to every update.",
            codeSnippet = "...",
            demoType = DemoType.COMBINE,
            explanationSteps = listOf(
                "Two flows emit values independently",
                "Latest value from both is combined",
                "Whenever any flow updates, new result is emitted",
                "Uses most recent values always",
                "Produces more frequent updates than zip"
            ),
            exampleInput = listOf("A,1 → A,2 → B,2"),
            exampleOutput = listOf("A1", "A2", "B2")
        ),

        // 🔹 BUFFER
        FlowOperator(
            id = "buffer",
            name = "buffer",
            description = "Adds buffer between producer and consumer.",
            impact = "Improves performance.",
            codeSnippet = "...",
            demoType = DemoType.BUFFER,
            explanationSteps = listOf(
                "Producer emits values quickly",
                "Consumer processes slowly",
                "Buffer stores intermediate values",
                "Producer continues without waiting",
                "Improves overall throughput"
            ),
            exampleInput = listOf("Fast: 1,2,3"),
            exampleOutput = listOf("Buffered processing")
        ),

        // 🔹 COLLECT LATEST
        FlowOperator(
            id = "collectLatest",
            name = "collectLatest",
            description = "Cancels previous work on new value.",
            impact = "Only latest work completes.",
            codeSnippet = "...",
            demoType = DemoType.COLLECT_LATEST,
            explanationSteps = listOf(
                "Flow emits values: A, B, C",
                "Processing starts for A",
                "B arrives and cancels A",
                "C arrives and cancels B",
                "Only C completes processing"
            ),
            exampleInput = listOf("A", "B", "C"),
            exampleOutput = listOf("Processed(C)")
        )
    )

    fun getOperatorById(id: String): FlowOperator? =
        getOperators().find { it.id == id }
}