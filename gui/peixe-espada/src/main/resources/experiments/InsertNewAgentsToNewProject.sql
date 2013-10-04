insert into configurationitem (id , baseurl, branchpath, trunkpath, idrepository, name)
values (nextval('configurationitem_seq'), 'file:///c:/Desenvolvimento/repositorio/iduff', 'branches', 'trunk', 1, 'IDUFF')

;
insert into softwareproject (id, mavenproject,repositoryurl, idconfigurationitem) values
(nextval('softwareproject_seq'), TRUE,
(select baseurl from configurationitem where name = 'IDUFF')||'/trunk',
(select ci.id from configurationitem ci where name = 'IDUFF'))

;
insert into projectuser (id, anonymous, login, password, idproject, idoceanouser ) values(
nextval('espada_agent_seq'), FALSE, 'any', 'any',
(select p.id from softwareproject p where p.repositoryurl = (select baseurl from configurationitem where name = 'IDUFF')||'/trunk'),
(select ou.id from oceanouser ou where ou.login = 'kann'))

;
insert into espada_agent (idagent, active, cycles, notimprovenorworsencycles, successcycles, worsencycles, idproject, idqualityattribute)
values (nextval('espada_agent_seq'), TRUE,  0, 0, 0, 0,
(select p.id from softwareproject p where p.repositoryurl = (select baseurl from configurationitem where name = 'IDUFF')||'/trunk'),
(select qa.id from espada_qualityattribute qa where name = 'EFFECTIVENESS'))

;
insert into espada_agent (idagent, active, cycles, notimprovenorworsencycles, successcycles, worsencycles, idproject, idqualityattribute)
values (nextval('espada_agent_seq'), TRUE,  0, 0, 0, 0,
(select p.id from softwareproject p where p.repositoryurl = (select baseurl from configurationitem where name = 'IDUFF')||'/trunk'),
(select qa.id from espada_qualityattribute qa where name = 'EXTENDIBILITY'))

;
insert into espada_agent (idagent, active, cycles, notimprovenorworsencycles, successcycles, worsencycles, idproject, idqualityattribute)
values (nextval('espada_agent_seq'), TRUE,  0, 0, 0, 0,
(select p.id from softwareproject p where p.repositoryurl = (select baseurl from configurationitem where name = 'IDUFF')||'/trunk'),
(select qa.id from espada_qualityattribute qa where name = 'FLEXIBILITY'))
;
