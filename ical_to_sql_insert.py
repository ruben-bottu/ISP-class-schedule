import ics
import sys
import re

#####################################
#### Reading from iCalendar file ####
#####################################

# UTF-8 necessary to deal with non-ASCII names correctly
with open(sys.argv[1], encoding="utf-8") as input:
    calendar = ics.Calendar(input.read())


# Ideally this method will become redundant and calendar.name will be used
def name(calendar):
    return calendar.extra[0].value


def get_group_name(calendar):
    match = re.search(r" - (.*)$", name(calendar))
    return match.group(1)


# corrections = {
#    'M-Beginselen van objectgeoriënteerd prog': 'M-Beginselen van objectgeoriënteerd programmeren'
# }


# Correct typos and other incorrect data
# def correct(object):
#    if object in corrections:
#        return corrections[object]
#    return object


def event_to_list(event):
    timestamp_format = "YYYY-MM-DD HH:mm:ss"
    start = event.begin.format(timestamp_format)
    end = event.end.format(timestamp_format)
    course_name = event.name  # correct(event.name)
    group_name = get_group_name(calendar)
    return [start, end, course_name, group_name]


def list_to_sql_row_constructor(list):
    joined = "', '".join(list)
    return f"('{joined}')"


#############################
#### Writing to sql file ####
#############################

with open(sys.argv[2], "a", encoding="utf-8") as output:
    nested_event_list = [event_to_list(event) for event in calendar.events]
    sql_row_constructor_list = [
        list_to_sql_row_constructor(event_list) for event_list in nested_event_list
    ]
    insert_values = ",\n".join(sql_row_constructor_list)
    insert_statement = f"INSERT INTO init_data (start_timestamp, end_timestamp, course_name, group_name) VALUES \n{insert_values};\n\n"

    output.write(insert_statement)
