
from todo.models import Item
from todo.models import Agent
from todo.models import ItemCollaborator


def create_item_collaborator(item_collaborator):
    try:
        item = Item.objects.get(item_id=item_collaborator.item_id)
        user = Agent.objects.get(collaborator_id=item_collaborator.collaborator_id)
        new_item_collaborator = ItemCollaborator(item_id=item.item_id, collaborator_id=user.collaborator_id)
        new_item_collaborator.save()
        return new_item_collaborator
    except Exception as e:
        return None


def delete_item_collaborator(item_id, collaborator_id):
    try:
        item_collaborator = ItemCollaborator.objects.get(item_id=item_id, collaborator_id=collaborator_id)
        item_collaborator.delete()
        return True
    except Exception as e:
        return False


def get_item_collaborators_for_item(item_id):
    collaborator_ids = [entry.collaborator_id for entry in ItemCollaborator.objects.filter(item_id=item_id)]
    item_collaborators = Agent.objects.filter(collaborator_id__in=collaborator_ids)
    return list(item_collaborators)
