
from todo.models import Item
from uuid import UUID


def get_item(item_id):
    try:
        item = Item.objects.get(item_id=item_id)
        return item
    except Exception as e:
        return None


def get_items():
    items = list(Item.objects.all())
    return list(items)


def create_item(new_item):
    new_item.set_field('item_id', UUID())
    new_item.save()
    return new_item


def update_item(item):
    try:
        item.save()
        return True
    except Exception as e:
        return False


def delete_item(item_id):
    try:
        item = Item.objects.get(item_id=item_id)
        item.delete()
        return True
    except Exception as e:
        return False


def get_items_for_agent(agent_id):
    return list(Item.objects.filter(agent_id=agent_id))


def get_item_by_title(title):
    try:
        item = Item.objects.get(title=title)
        return item
    except Exception as e:
        return None

