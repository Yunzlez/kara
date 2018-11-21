create table bin_config (
  bin_id int primary key auto_increment,
  config_key varchar(64) not null,
  config boolean not null
)