package com.alexeycode.kboy

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Scope
import kotlinx.benchmark.State

@State(Scope.Benchmark)
open class CpuBenchmark {

    @Benchmark
    fun benchmark() {

    }

}