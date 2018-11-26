create table bin_config (
  bin_id int primary key auto_increment,
  config_key varchar(64) not null,
  config bit(1) not null
);

insert into bin_config (bin_id, config_key, config)
select id, 'isPermanent' as config_key, TRUE as config from bin where permanent = TRUE;

alter table bin
drop column permanent;