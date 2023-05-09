### Database document
> 2023-03-29 20:19:36
#### qrxsupplierinsurance 保险公司
NO | KEY | COLUMN | COMMENT | DATA_TYPE | NOTNULL | REMARK
:---: | :---: | --- | --- | --- | :---: | ---
1|PRI|ID|主键id|INT|Y|
2| |SUPPLIERID|保险公司id|INT UNSIGNED|Y|
3| |INSURANCEID|险种编码|VARCHAR(20)|Y|
4| |SUPPLIERNAME|供应商名称|VARCHAR(100)|Y|
5| |DEFAULTQUOTA|默认出现率|DECIMAL(12,2) UNSIGNED|N|
6| |EXTRAQUOTA|额外净损额|DECIMAL(12,2) UNSIGNED|N|
7| |EXTRABLACKRATE|黑名单为1导致的保额上涨比例|DECIMAL(12,2)|N|
8| |STATUS|状态：1启用；0关闭|SMALLINT|N|
9| |LASTUPDATE|最后修改时间|TIMESTAMP|Y|
10| |CREATETIME|创建时间|DATETIME|Y|
11| |POLICYEFFECTTYPE|保单生效类型：1 出库生效 2订单完成生效 3订单完成后N天生效 4 厂保结束后生效 (如果为3，policyDelayDays不能为空)|TINYINT(1)|N|
12| |POLICYDELAYDAYS|保单延迟生效天数，如果policyEffectType=3，此值必填|INT|N|
13| |BZDAYS|保障期限(保障开始时间为保单生效时间，结束时间为保单生效时间+保障期限)|INT|N|
14| |CLAIMEFFECTTYPE|履约生效类型：0 保单生效时间 1 出库生效 2订单完成生效 3订单完成后N天生效 4 厂保结束后生效 (如果为3，claimDelayDays不能为空)|TINYINT(1)|N|
15| |CLAIMDELAYDAYS|履约延迟生效天数，如果claimEffectType=3，此值必填|INT|N|
16| |CLAIMDAYS|履约期限(结束时间为履约生效时间+履约期限)|INT|N|
17| |CLAIMTIMES|履约次数|INT|N|
18| |BIZTYPE|数据力业务类型|VARCHAR(40)|N|
19| |SETTLESKU|结算系统sku，老保险公司(131,132,133)insuranceid，新的保险公司supplierid_insuranceid|VARCHAR(20)|N|
20| |SETTLESKUNAME|结算系统sku名称|VARCHAR(40)|N|
