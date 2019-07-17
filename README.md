# spring-activiti
Activiti6 + Spring Boot 2 integration.

---
### Design Target
1. 统一流程定义管理
2. 统一任务处理操作
3. 统一历史任务查询
4. 流程中间变量规范

---
### 模块组成
1. workflow-engine：流程引擎，提供服务
2. workflow-engine-api：流程引擎交互契约，使用feign进行契约定义
3. client：模拟客户端

---
### 引擎提供的服务

#### 流程管理模块
- 流程部署
- 流程启动
- 流程挂起
- 流程恢复

#### 任务管理模块


#### 历史查询模块

