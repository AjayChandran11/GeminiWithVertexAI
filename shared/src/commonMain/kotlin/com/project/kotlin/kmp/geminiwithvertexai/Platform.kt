package com.project.kotlin.kmp.geminiwithvertexai

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform