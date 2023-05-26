insert into dbo.exercises_info values
                                   ('40c806c4-d4e7-472f-88f8-ed547e8afd12', 'Push ups', 'Test Description', 'Arms'),
                                   ('40c806c4-d4e7-472f-88f8-ed547e8afd13', 'Push ups', 'Test Description', 'Arms');

select distinct p.id, p.monitor_id, p.title
from dbo.plans p
         inner join dbo.client_plans cp on p.id = cp.plan_id
         inner join dbo.daily_lists dl on dl.plan_id = p.id
where cp.client_id = '8e8aaf90-e3ac-41f6-9569-001eaf10fa68' and '2023-05-29' >= cp.dt_start
  and '2023-05-29' <= (cp.dt_start + dl.index * interval '1 day')
