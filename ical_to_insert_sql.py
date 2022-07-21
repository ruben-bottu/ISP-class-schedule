import ics
import sys
import re

#####################################
#### Reading from iCalendar file ####
#####################################

# utf-8 necessary to deal with accented names correctly
with open(sys.argv[1], encoding='utf-8') as input:
    calendar = ics.Calendar(input.read())


# Ideally this method will become redundant and calendar.name will be used
def name(calendar):
    return calendar.extra[0].value


def get_class_group_name(calendar):
    match = re.search(r' - (.*)$', name(calendar))
    return match.group(1)


# corrections = {
#    'M-Beginselen van objectgeoriënteerd prog': 'M-Beginselen van objectgeoriënteerd programmeren'
# }


# Correct typos and other incorrect data
# def correct(object):
#    if object in corrections:
#        return corrections[object]
#    return object


def event_to_row_constructor(event):
    timestamp_format = 'YYYY-MM-DD HH:mm:ss'
    start = event.begin.format(timestamp_format)
    end = event.end.format(timestamp_format)
    course_name = event.name  # correct(event.name)
    class_group_name = get_class_group_name(calendar)
    # Add "," at the end?
    return f"('{start}', '{end}', '{course_name}', '{class_group_name}')"


#############################
#### Writing to sql file ####
#############################

with open(sys.argv[2], 'a', encoding='utf-8') as output:
    for event in calendar.events:
        # Remove \n in final version?
        # The entire "+ ',\n'" here is inefficient since the entire string returned by event_to_row_constructor() needs to be copied
        output.write(event_to_row_constructor(event) + ',\n')
