#!/usr/bin/bash
cd ~/ROOT
echo "START $(date --iso-8601=seconds)" &> ~/log/update_daily_events.log
npm ci &>> ~/log/update_daily_events.log
# drop expired events from json, make json of dropped IDs
npm run drop_file &>> ~/log/update_daily_events.log
# drop expired events from database
timeout 20 npm run drop_db &>> ~/log/update_daily_events.log
#add new events to JSON
npm run get_events &>> ~/log/update_daily_events.log
#use JSON to add new events to database
timeout 20 npm run add_to_db &>> ~/log/update_daily_events.log
#drop any events that are in our up to date db but not in the site itself (cancelled?)
npm run drop_cancelled  &>> ~/log/update_daily_events.log
#drop those events
timeout 20 npm run drop_db &>> ~/log/update_daily_events.log
echo "END $(date --iso-8601=seconds)" &>> ~/log/update_daily_events.log
