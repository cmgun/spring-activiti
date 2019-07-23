# spring-activiti
Activiti6 + Spring Boot 2 + Spring Cloud Greenwich integration.

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
- 流程删除

#### 任务管理模块
- 待办任务查询
- 任务认领
- 任务取消认领
- 任务完成
- 改变任务待办分配组

#### 历史查询模块
- 根据业务key查询历史审批任务
- 根据用户组查询历史审批任务
- 根据用户查询历史审批任务

---
### 流程定义规范
**1、流程定义的限制**

由于流程引擎独立于业务系统，使用ServiceTask和ScriptTask都强耦合于业务代码。
一期开发从简为主，因此不设计复杂的系统交互流程。

**2、当前支持基本元素**
1. Start Event   
1.1. StartEvent
2. End Event   
2.1. EndEvent
3. Task   
3.1. UserTask
4. Gateway   
4.1. ExclusiveGateway

---
### 引擎基础设施
- 请求日志记录
- 请求流水号防重（Redis）
- 请求流水记录，支持请求补偿查询

