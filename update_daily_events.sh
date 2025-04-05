#!/usr/bin/bash
cd ~/ROOT

# log all stdout and stderr into ~/log/update_daily_events.log, as well as printing to screen
echo "START $(date --iso-8601=seconds)" |& tee ~/log/update_daily_events.log
npm ci                                  |& tee -a ~/log/update_daily_events.log
npm run drop                            |& tee -a ~/log/update_daily_events.log
npm run get_events                      |& tee -a ~/log/update_daily_events.log
npm run add_to_db                       |& tee -a ~/log/update_daily_events.log
echo "END $(date --iso-8601=seconds)"   |& tee -a ~/log/update_daily_events.log
