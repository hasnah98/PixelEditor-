# 🎨 Pixel Editor Pro – Java Image Filter Application

**Pixel Editor Pro** is a Java Swing–based desktop application that demonstrates real-time image filtering using pixel-level manipulation. The project focuses on clean UI design, background processing with `SwingWorker`, and animation using `Timer`.

This repository is ideal for learning:
- Java Swing UI design
- Image processing at pixel level
- Multithreading in Swing applications
- Simple animation techniques

---

## ✨ Features

🖼️ Load and display images using `JFileChooser`

🔍 Side-by-side image preview (Image A & Image B)

🧪 **Step Filter**: Apply the filter once to Image A

▶️ **Animated Filter**: Continuously apply the filter to Image B

⚡ Background processing using `SwingWorker` (non-blocking UI)

🎨 Modern UI styling with borders, fonts, and color themes

🖥️ Automatic image scaling to fit display panels

---

## 🛠️ Technologies Used

- **Java SE**
- **Swing (GUI)**
- **AWT Graphics**
- **BufferedImage**
- **SwingWorker & Timer**

---

## 📂 Project Structure

```text
FilterPicture.java
```

> The entire application is contained in a single class for simplicity and educational clarity.

---

## 🚀 How to Run

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/your-username/Pixel-Editor-Pro.git
```

### 2️⃣ Compile

```bash
javac FilterPicture.java
```

### 3️⃣ Run

```bash
java FilterPicture
```

---

## 🎛️ How It Works

### Image Loading

- A single image is loaded and duplicated for:
  - **Image A** (manual filter)
  - **Image B** (animated filter)

### Filter Logic

The filter modifies RGB values per pixel:

```java
r = (r + 8) % 256;
g = (g + 5) % 256;
b = (b + 12) % 256;
```

This creates a gradual color-shifting visual effect.

### Threading

- `SwingWorker` is used to apply filters without freezing the UI
- `Timer` repeatedly applies the filter for animation

---

## 📸 UI Overview

- **Top Bar**: Application title & image loader
- **Left Panel (Image A)**:
  - Manual filter using **Step** button
- **Right Panel (Image B)**:
  - Continuous filtering using **Start / Stop** button

---

## 📌 Future Improvements

- Add multiple filter types (Grayscale, Blur, Edge Detection)
- Dark mode support
- Undo / Reset filter
- Save filtered image
- Progress indicator during processing


---

## 📄 License

This project is licensed under the **MIT License** – feel free to use and modify it for learning or personal projects.

