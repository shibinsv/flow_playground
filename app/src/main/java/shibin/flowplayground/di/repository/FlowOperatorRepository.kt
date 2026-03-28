package shibin.flowplayground.di.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
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
            impact = "1-to-1 transformation.",
            codeSnippet = """
                flowOf(1, 2, 3)
                    .map { it * 2 }
                    .collect { println(it) }
            """.trimIndent(),
            demoType = DemoType.MAP,
            explanationSteps = listOf("1 → 2", "2 → 4", "3 → 6"),
            exampleInput = listOf("1", "2", "3"),
            exampleOutput = listOf("2", "4", "6")
        ),

        // 🔹 FILTER
        FlowOperator(
            id = "filter",
            name = "filter",
            description = "Emits only values that satisfy the given predicate.",
            impact = "Filters out unwanted values.",
            codeSnippet = """
                flowOf(1, 2, 3, 4, 5)
                    .filter { it % 2 == 0 }
                    .collect { println(it) }
            """.trimIndent(),
            demoType = DemoType.FILTER,
            explanationSteps = listOf("Only even numbers pass"),
            exampleInput = listOf("1", "2", "3", "4", "5"),
            exampleOutput = listOf("2", "4")
        ),

        // 🔹 FLATMAP LATEST
        FlowOperator(
            id = "flatMapLatest",
            name = "flatMapLatest",
            description = "Cancels previous flow when new value arrives.",
            impact = "Only latest flow is active.",
            codeSnippet = """
                flowOf("A", "B", "C")
                    .onEach { delay(100) }
                    .flatMapLatest {
                        flow {
                            emit("Start ${'$'}it")
                            delay(200)
                            emit("End ${'$'}it")
                        }
                    }
                    .collect { println(it) }
            """.trimIndent(),
            demoType = DemoType.FLAT_MAP_LATEST,
            explanationSteps = listOf("Previous flow cancelled when new value comes"),
            exampleInput = listOf("A", "B", "C"),
            exampleOutput = listOf("Start C", "End C")
        ),

        // 🔹 FLATMAP MERGE
        FlowOperator(
            id = "flatMapMerge",
            name = "flatMapMerge",
            description = "Runs multiple flows in parallel.",
            impact = "Concurrent execution.",
            codeSnippet = """
                flowOf(1, 2, 3)
                    .flatMapMerge {
                        flow {
                            emit("${'$'}it-start")
                            delay(100)
                            emit("${'$'}it-end")
                        }
                    }
                    .collect { println(it) }
            """.trimIndent(),
            demoType = DemoType.FLAT_MAP_MERGE,
            explanationSteps = listOf("All flows run concurrently"),
            exampleInput = listOf("1", "2", "3"),
            exampleOutput = listOf("1-start", "2-start", "3-start")
        ),

        // 🔹 DEBOUNCE
        FlowOperator(
            id = "debounce",
            name = "debounce",
            description = "Emits only after delay with no new values.",
            impact = "Suppresses rapid emissions.",
            codeSnippet = """
                flow {
                    emit("h")
                    delay(100)
                    emit("he")
                    delay(100)
                    emit("hel")
                    delay(400)
                    emit("hello")
                }
                .debounce(300)
                .collect { println(it) }
            """.trimIndent(),
            demoType = DemoType.DEBOUNCE,
            explanationSteps = listOf("Only emits after pause"),
            exampleInput = listOf("h", "he", "hel", "hello"),
            exampleOutput = listOf("hello")
        ),

        // 🔹 DISTINCT
        FlowOperator(
            id = "distinctUntilChanged",
            name = "distinctUntilChanged",
            description = "Removes consecutive duplicates.",
            impact = "Avoids redundant emissions.",
            codeSnippet = """
                flowOf(1, 1, 2, 2, 3)
                    .distinctUntilChanged()
                    .collect { println(it) }
            """.trimIndent(),
            demoType = DemoType.DISTINCT_UNTIL_CHANGED,
            explanationSteps = listOf("Duplicates removed"),
            exampleInput = listOf("1", "1", "2", "2", "3"),
            exampleOutput = listOf("1", "2", "3")
        ),

        // 🔹 ZIP
        FlowOperator(
            id = "zip",
            name = "zip",
            description = "Pairs values from two flows.",
            impact = "Synchronizes emissions.",
            codeSnippet = """
                val flow1 = flowOf("A", "B", "C")
                val flow2 = flowOf(1, 2, 3)

                flow1.zip(flow2) { a, b -> "${'$'}a${'$'}b" }
                    .collect { println(it) }
            """.trimIndent(),
            demoType = DemoType.ZIP,
            explanationSteps = listOf("Pairs values one by one"),
            exampleInput = listOf("A+1", "B+2", "C+3"),
            exampleOutput = listOf("A1", "B2", "C3")
        ),

        // 🔹 COMBINE
        FlowOperator(
            id = "combine",
            name = "combine",
            description = "Combines latest values from flows.",
            impact = "Reacts to every update.",
            codeSnippet = """
                val flow1 = flowOf("A", "B")
                val flow2 = flowOf(1, 2)

                flow1.combine(flow2) { a, b -> "${'$'}a${'$'}b" }
                    .collect { println(it) }
            """.trimIndent(),
            demoType = DemoType.COMBINE,
            explanationSteps = listOf("Uses latest values"),
            exampleInput = listOf("A,1 → A,2 → B,2"),
            exampleOutput = listOf("A1", "A2", "B2")
        ),

        // 🔹 BUFFER
        FlowOperator(
            id = "buffer",
            name = "buffer",
            description = "Adds buffer between producer and consumer.",
            impact = "Improves performance.",
            codeSnippet = """
                flow {
                    repeat(3) {
                        delay(100)
                        emit(it)
                    }
                }
                .buffer()
                .collect {
                    delay(300)
                    println(it)
                }
            """.trimIndent(),
            demoType = DemoType.BUFFER,
            explanationSteps = listOf("Producer runs faster than consumer"),
            exampleInput = listOf("1,2,3"),
            exampleOutput = listOf("Buffered output")
        ),

        // 🔹 COLLECT LATEST
        FlowOperator(
            id = "collectLatest",
            name = "collectLatest",
            description = "Cancels previous work on new value.",
            impact = "Only latest work completes.",
            codeSnippet = """
                flowOf("A", "B", "C")
                    .collectLatest {
                        println("Start ${'$'}it")
                        delay(200)
                        println("End ${'$'}it")
                    }
            """.trimIndent(),
            demoType = DemoType.COLLECT_LATEST,
            explanationSteps = listOf("Previous work cancelled"),
            exampleInput = listOf("A", "B", "C"),
            exampleOutput = listOf("Only C completes")
        )
    )

    fun getOperatorById(id: String): FlowOperator? =
        getOperators().find { it.id == id }
}