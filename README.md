# AlgoLearn 🚀

**AlgoLearn** is an interactive desktop application that brings sorting and searching algorithms to life through dynamic, step-by-step visualizations. 

Built as a showcase of clean software engineering practices, this project demonstrates a strong grasp of **Object-Oriented Programming (OOP)**, **Design Patterns**, and **Data Structures & Algorithms**.

## 🎯 Key Features
* **Live Visualization:** Watch algorithms execute in real-time with color-coded bar charts (amber for active swaps/comparisons, green for targets).
* **Algorithm Library:** Includes implementations of Bubble, Selection, Insertion, Merge, and Quick Sort, alongside Linear and Binary Search.
* **Playback Control:** Full control over the simulation with Play, Pause, Step Forward/Backward, and adjustable animation speeds.
* **Educational Insights:** Real-time tracking of comparisons and swaps, coupled with Time/Space complexity analysis and inline pseudocode.
* **Data Persistence:** Utilizes Java Object Serialization to maintain a persistent history of all simulation sessions.

## 🏗️ Technical Architecture
This project was built with a strict adherence to clean architecture principles to ensure decoupled, maintainable, and scalable code:
* **MVC Architecture:** Clear separation of concerns across Model (`AlgorithmEngine`), View (`VisualizationPane`), and Controller (`SimulationController`) layers.
* **Observer Pattern:** Implemented custom `Observable` interfaces to handle state changes and UI updates asynchronously without tight coupling.
* **Strategy Pattern:** Utilized `AlgorithmStrategy` interfaces to allow seamless, runtime switching between different sorting and searching algorithms.

## 💻 Technologies & Tools
* **Language:** Java (Streams, Lambdas, Functional Interfaces)
* **UI Framework:** JavaFX (Custom Panes, Timeline Animations)
* **Data Management:** Java Object Serialization

## 🚀 Getting Started
1. Clone the repository: `git clone https://github.com/your-username/AlgoLearn.git`
2. Open the project in your preferred Java IDE (e.g., Eclipse, IntelliJ IDEA).
3. Ensure JavaFX is configured in your build path.
4. Run `AlgoLearnApp.java` to launch the simulator.

