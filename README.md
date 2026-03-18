# 📌 Hilt 실습 Todo 앱 (Room + MVVM)

## 🧩 프로젝트 소개

기존에 진행했던 MVVM 기반 Todo 앱(todo_mvvm_kotlin) 프로젝트를 확장하여,
의존성 주입 라이브러리인 Hilt를 적용한 프로젝트입니다.

단순 기능 구현을 넘어서, 객체 생성과 의존성 관리 방식을 개선하는 데 목적을 두었습니다.

---

## 🏗 프로젝트 구조
본 프로젝트는 **MVVM 아키텍처**, **Hilt**, **Room**을 기반으로 데이터 흐름을 계층화하여 구성되었습니다.

* **View (Activity)**: 사용자 인터페이스 및 상호작용 담당
* **ViewModel**: UI 상태 관리 및 Repository 연결
* **Repository**: 데이터 소스 제어 (중간 계층)
* **DAO**: 데이터베이스 접근 인터페이스
* **Room Database**: 실제 데이터 저장소

---

## ⚙️ Hilt 적용 이유
기존 방식에서는 객체 생성 및 의존성 연결을 개발자가 직접 관리해야 했으나, **Hilt**를 도입함으로써 다음과 같은 이점을 얻었습니다.
* **의존성 주입(DI) 자동화**: 보일러플레이트 코드 감소
* **결합도 저하**: 코드 간의 의존성을 줄여 유지보수 및 테스트 용이성 향상

---

## 🏗️ 주요 구성 요소

### 1️⃣ Room Database
앱의 로컬 데이터베이스 역할을 하며, DAO를 통해 데이터에 접근합니다.

```kotlin
@Database(entities = [TodoEntity::class], version = 1)
abstract class TodoDataBase : RoomDatabase() {
    abstract fun dao(): TodoDao
}
```

### 2️⃣ Hilt Module (DatabaseModule)
Room은 외부 라이브러리이므로 Hilt가 스스로 생성 방법을 알 수 없습니다. 따라서 `@Provides`를 통해 객체 생성 방법을 명시합니다.

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TodoDataBase {
        return Room.databaseBuilder(
            context,
            TodoDataBase::class.java,
            "todo_database"
        ).build()
    }

    @Provides
    fun provideTodoDao(dataBase: TodoDataBase): TodoDao {
        return dataBase.dao()
    }
}
```
>  Database는 `@Singleton`으로 앱 전역에서 하나만 존재하도록 관리하며, DAO는 생성된 Database 인스턴스를 통해 제공됩니다.

### 3️⃣ Repository
데이터 처리를 담당하는 계층으로, DAO를 주입받아 사용합니다.

```kotlin
@Singleton
class TodoRepository @Inject constructor(
    private val dao: TodoDao
)
```
*  직접 만든 클래스이므로 `@Inject constructor`를 사용하여 Hilt가 자동으로 생성하도록 설정합니다. 별도의 `@Provides` 설정이 필요 없습니다.

### 4️⃣ ViewModel
UI와 데이터를 연결하며 Repository를 주입받습니다.

```kotlin
@HiltViewModel
class TodoViewModel @Inject constructor(
    private val repo: TodoRepository
) : ViewModel()
```
*  `@HiltViewModel` 어노테이션을 통해 Hilt가 ViewModel의 생명주기를 자동으로 관리합니다.

### 5️⃣ View (Activity)
Hilt를 사용할 Android 컴포넌트임을 명시합니다.

```kotlin
@AndroidEntryPoint
class MainActivity : AppCompatActivity()
```
*  `@AndroidEntryPoint`가 선언된 곳에서만 Hilt를 통한 의존성 주입이 가능합니다.

---

## 🔄 Hilt 의존성 생성 흐름

### 📌 의존성 요구 흐름 (Bottom-Up)
사용 시점에는 아래와 같은 역순으로 객체를 요구하게 됩니다.
1. `ViewModel` 필요
2. → `Repository` 필요
3. → `TodoDao` 필요
4. → `TodoDataBase` 필요

### 📌 실제 생성 순서 (Top-Down)
Hilt는 의존성 관계를 파악하여 순차적으로 객체를 생성합니다.
1. **Database 생성 완료**
2. **`provideTodoDao` 실행** (DAO 생성)
3. **`Repository` 생성** (DAO 주입)
4. **`ViewModel` 생성** (Repository 주입)

---

## 💡 핵심 요약
| 구분 | 대상 | 사용 어노테이션 |
| :--- | :--- | :--- |
| **외부 라이브러리** | Room, Retrofit 등 | `@Module`, `@Provides` |
| **내부 클래스** | Repository, UseCase 등 | `@Inject constructor` |
| **Android 컴포넌트** | Activity, Fragment | `@AndroidEntryPoint` |
| **ViewModel** | Architecture Component | `@HiltViewModel` |

---

## 🚀 배운 점
* DI(의존성 주입) 개념이 실제 코드에서 객체 간의 결합도를 어떻게 낮추는지 체득했습니다.
* Hilt를 활용해 복잡한 객체 생성 로직을 자동화하는 효율적인 방식을 학습했습니다.
* **Room + Hilt + MVVM** 구조의 유기적인 연결 흐름을 이해하고 실무적인 아키텍처 설계 능력을 키웠습니다.

---





