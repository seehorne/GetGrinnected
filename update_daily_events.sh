#!/usr/bin/bash
cd ~/ROOT
echo "running script"
echo "START $(date --iso-8601=seconds)" &> ~/log/update_daily_events.log
npm ci &>> ~/log/update_daily_events.log
# drop expired events from json, make json of dropped IDs
echo "started JSON drop for expired at $(date --iso-8601=seconds)"
npm run drop_file &>> ~/log/update_daily_events.log
echo "finished JSON drop for expired at $(date --iso-8601=seconds)"
# drop expired events from database
echo "started db drop for expired at $(date --iso-8601=seconds)"
timeout 30 npm run drop_db &>> ~/log/update_daily_events.log
echo "finished db drop for expired at $(date --iso-8601=seconds)"
#drop any events that are in our up to date db but not in the site itself (cancelled?)
echo "started JSON drop for cancelled at $(date --iso-8601=seconds)"
npm run drop_cancelled  &>> ~/log/update_daily_events.log
echo "finished JSON drop for cancelled at $(date --iso-8601=seconds)"
#drop those events
echo "started db drop for cancelled at $(date --iso-8601=seconds)"
timeout 30 npm run drop_db &>> ~/log/update_daily_events.log
echo "finished db drop for cancelled at $(date --iso-8601=seconds)"
#add new events to JSON
echo "started JSON add for events at $(date --iso-8601=seconds)"
npm run get_events &>> ~/log/update_daily_events.log
echo "finished JSON add for events at $(date --iso-8601=seconds)"
#use JSON to add new events to database
echo "started db add for events at $(date --iso-8601=seconds)"
timeout 30 npm run add_to_db &>> ~/log/update_daily_events.log
echo "finished db add for events at $(date --iso-8601=seconds)"
echo "END $(date --iso-8601=seconds)" &>> ~/log/update_daily_events.log
