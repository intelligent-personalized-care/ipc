insert into dbo.exercises_info values
                                   ('40c806c4-d4e7-472f-88f8-ed547e8afd12', 'Push ups', 'Test Description', 'Arms'),
                                   ('40c806c4-d4e7-472f-88f8-ed547e8afd13', 'Push ups', 'Test Description', 'Arms');

insert into dbo.monitor_rating values
                                   ('d137ac09-d07a-4998-8236-c04013ec6a07', '53f3c2b1-9c19-448e-aa56-a4753e41ea96', 5),
                                   ('d137ac09-d07a-4998-8236-c04013ec6a07', '650ec4c5-240f-41a6-845a-54f690efeb6f', 2);


select exists (
               select 1
               from dbo.plans p inner join dbo.client_plans cp on p.id = cp.plan_id
               where cp.client_id = :clientID
                   and
                     (((:startDate between cp.dt_start and (cp.dt_start + (select max(dl.index) from dbo.daily_lists dl where dl.plan_id = p.id)))
                       or (:endDate between cp.dt_start and (cp.dt_start + (select max(dl.index) from dbo.daily_lists dl where dl.plan_id = p.id))))
                  or (cp.dt_start between :startDate and :endDate
                   and (cp.dt_start + (select max(dl.index) from dbo.daily_lists dl where dl.plan_id = p.id)) between :startDate and :endDate)
           ))