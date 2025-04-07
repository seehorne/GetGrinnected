#!/usr/bin/bash
cd ~/ROOT
echo "START $(date --iso-8601=seconds)" &> ~/log/update_daily_events.log
npm ci &>> ~/log/update_daily_events.log
# drop expired events from json, make json of dropped IDs
npm run drop_file &>> ~/log/update_daily_events.log
# drop expired events from database
timeout 10 npm run drop_db &>> ~/log/update_daily_events.log
#add new events to JSON
npm run get_events &>> ~/log/update_daily_events.log
#use JSON to add new events to database
timeout 10 npm run add_to_db &>> ~/log/update_daily_events.log
echo "END $(date --iso-8601=seconds)" &>> ~/log/update_daily_events.log
