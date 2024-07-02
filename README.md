# Open AI를 이용한 자기소개서 기반 가상 면접 도우미

## 목차
1. [프로젝트 개요](#프로젝트-개요)
2. [주요 기능](#주요-기능)
3. [시스템 아키텍처](#시스템-아키텍처)
4. [데이터베이스 설계](#데이터베이스-설계)
5. [화면 구성](#화면-구성)
6. [프로젝트 실행 방법](#프로젝트-실행-방법)
7. [개발 후기](#개발-후기)
8. [라이센스](#라이센스)
9. [기여 방법](#기여-방법)

## 프로젝트 개요

사용자의 자기소개서를 기반으로 Open AI가 분석한 면접 질문을 바탕으로 가상 면접을 도와주는 웹 애플리케이션입니다.

- **개발 기간**: 2024.05.28 ~ 
- **개발 인원**: 2명 (백엔드 개발)
- **주요 기능**: 회원 관리, 자기소개서 CRUD, AI 면접 질문 생성, 가상 면접 진행
- **기술 스택**: 
  - Backend: Java, Spring Boot, Spring Data JPA
  - Frontend: Thymeleaf, HTML, CSS, JavaScript
  - Database: H2
  - API: Open AI API

## 주요 기능

1. **회원 관리**
   - 회원가입 및 로그인 기능
   - 유효성 검증 기능

2. **자기소개서 관리**
   - 자기소개서 등록, 조회, 수정, 삭제
   - 파일 업로드 지원

3. **면접 질문 생성 및 관리**
   - Open AI API를 활용한 자기소개서 기반 면접 질문 자동 생성
   - 사용자 직접 질문 추가 기능
   - 생성된 질문 저장 및 관리

4. **가상 면접 진행**
   - 저장된 질문을 바탕으로 가상 면접 진행
   - 면접 결과 저장 및 다시보기 기능

5. **사용자 친화적 UI/UX**
   - 직관적인 인터페이스 제공
   - 반응형 웹 디자인

## 시스템 아키텍처

```
[Client]
    ↕ HTTP
[Spring Boot Application]
    |
    ├─ [Controllers]
    |   ├─ UserController
    |   ├─ InterviewController
    |   └─ InterviewReadyController
    |
    ├─ [Services]
    |   ├─ UserService
    |   ├─ CoverLetterService
    |   └─ QuestionService
    |
    ├─ [Repositories]
    |   ├─ UserRepository
    |   ├─ CoverLetterRepository
    |   └─ QuestionRepository
    |
    └─ [Entities]
        ├─ User
        ├─ CoverLetter
        └─ Question

[Database (H2 or other)]

[External Services]
    ↕ API
[OpenAI API]
```

## 데이터베이스 설계


![스크린샷 2024-06-15 오전 1 36 36](https://github.com/seungwoo-project/AI-Interview-Coach/assets/112483124/69ab60ba-f716-4803-8e8f-985cadbf3f72)


## 화면 구성

1. 로그인 화면
2. 회원가입 화면
3. 자기소개서 목록 화면
4. 자기소개서 상세 조회 화면
5. 면접 준비 화면 (질문 선택, 추가)
6. AI 면접 진행 화면
7. 면접 결과 및 다시보기 화면

## 프로젝트 실행 방법

1. 저장소 클론
   ```
   git clone https://github.com/seungwoo-project/AIInterview-Coach.git
   ```
2. 프로젝트 디렉토리로 이동
   ```
   cd AIInterview-Coach
   ```
3. 의존성 설치
   ```
   ./gradlew build
   ```
4. 애플리케이션 실행
   ```
   ./gradlew bootRun
   ```

## 개발 후기

- Spring, JPA, Thymeleaf 등의 기술을 실제 프로젝트에 적용하며 실력 향상
- 프로젝트 설계의 중요성 인식 및 체계적인 설계 방법 학습
- Open AI API 연동 등 새로운 기술에 대한 도전과 성공 경험
- 팀 프로젝트를 통한 협업 능력 향상
- 실제 서비스 개발 과정에서의 문제 해결 능력 향상

## 라이센스

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## 기여 방법

1. 이 저장소를 포크합니다.
2. 새 브랜치를 생성합니다 (`git checkout -b feature/AmazingFeature`)
3. 변경 사항을 커밋합니다 (`git commit -m 'Add some AmazingFeature'`)
4. 브랜치에 푸시합니다 (`git push origin feature/AmazingFeature`)
5. Pull Request를 생성합니다.
