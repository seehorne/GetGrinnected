#!/usr/bin/bash
cd ~/ROOT
echo "START $(date --iso-8601=seconds)" &> ~/log/update_daily_events.log
npm ci &>> ~/log/update_daily_events.log
npm run drop &>> ~/log/update_daily_events.log
npm run get_events &>> ~/log/update_daily_events.log
npm run add_to_db &>> ~/log/update_daily_events.log
echo "END $(date --iso-8601=seconds)" &>> ~/log/update_daily_events.log
