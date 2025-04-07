package com.project.kotlin.kmp.geminiwithvertexai.service

import com.project.kotlin.kmp.geminiwithvertexai.data.Content
import com.project.kotlin.kmp.geminiwithvertexai.data.GeminiRequest
import com.project.kotlin.kmp.geminiwithvertexai.data.GeminiResponse
import com.project.kotlin.kmp.geminiwithvertexai.data.Part
import com.project.kotlin.kmp.geminiwithvertexai.remote.httpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class GeminiApiService(private val projectId: String = "gsoc-proposal-2025",
                       private val location: String = "asia-south1") {
    private val client by lazy {
        httpClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                })
            }
        }
    }

    suspend fun generateContent(
        modelName: String = "gemini-2.0-flash",
        prompt: String,
        apiKey: String
    ): String = withContext(Dispatchers.Default) {
        val response: GeminiResponse = client.post(
            "https://${location}-aiplatform.googleapis.com/v1/projects/${projectId}/locations/${location}/publishers/google/models/${modelName}:generateContent"
        ) {
            header("Authorization", "Bearer $apiKey")
            header("Content-Type", "application/json")
            setBody(GeminiRequest(contents = listOf(Content(parts = listOf(Part(text = prompt))))))
        }.body()

        response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: throw Exception("No response from Gemini")
    }
}