create table bin_config (
  bin_id bigint(20) not null,
  config_key varchar(64) not null,
  config bit(1) not null,
  primary key (bin_id, config_key))
  engine = MyISAM;

insert into bin_config (bin_id, config_key, config)
select id, 'isPermanent' as config_key, TRUE as config from bin where permanent = TRUE;

alter table bin
drop column permanent;