# Miku 💙

**A local-first AI desktop companion for Windows.**

Miku is an experimental AI desktop companion designed to feel like a friendly presence on your computer. The long-term goal is to combine natural conversations, customizable personalities, contextual screen awareness, and useful desktop interactions.

## Project Status

🚧 **Early development — prototype**

### Currently implemented

- JavaFX desktop chat interface.
- Local AI integration using Ollama.
- Support for the llama3.2:3b model.
- Asynchronous AI responses using Java HTTP Client.

### Planned features

- Floating animated desktop companion.
- Smart contextual screen awareness with user-controlled privacy settings.
- Vision model integration.
- Voice input and output.
- Customizable AI personalities.
- Desktop actions with user confirmation.

## Tech Stack

- Java 21
- JavaFX
- Maven
- Ollama
- Llama 3.2 3B
- Jackson

## Requirements

- Windows 10/11
- Java JDK 21 or later
- Maven
- Ollama

## Getting Started

### 1. Install and start Ollama

Download Ollama from [ollama.com](https://ollama.com).

Pull the model:

```bash
ollama pull llama3.2:3b
