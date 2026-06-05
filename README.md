# Rent-a-Car-System
2026 데이터베이스 파티모집팀 프로젝트 

##### 사전 요구사항

- Java17 이상 설치

실행용 폴더 내부에 있는 .jar파일 혹은 run.bat 파일을 통해 실행할 수 있습니다. 
jdk 설치가 필요합니다. 
또한, db서버 연결을 위해 서버가 켜져 있는 상태에서만 연결이 가능합니다. 

---

### 로컬 사용 가이드

#### 사전 요구사항

- MySQL 혹은 MariaDB 설치
- Java17 이상 설치

1. 'Rent-a-Car-System/Rent_a_Car/sql/setup.sql' 파일을 HeidiSQL에서 실행하여 데이터베이스, 테이블, 더미데이터를 생성해주세요.
2. 이후 'Rent-a-Car-System/실행용jar파일/db.properties' 파일의
   db.url=jdbc:mysql://'YOUR_IP':3306/rental_car
   db.user='YOUR_USER'
   db.password='YOUR_PASSWORD'
   를 자신에게 맞게 수정해주세요.
4. 같은 폴더 내부에 존재하는 '.jar'파일 혹은 'run.bat' 파일을 실행해주세요. (java가 없다면 실행되지 않습니다.)
